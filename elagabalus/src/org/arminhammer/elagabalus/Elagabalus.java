/**
 * 
 */
package org.arminhammer.elagabalus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInputStream;
import com.esotericsoftware.kryo.io.ByteBufferOutputStream;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
//import com.esotericsoftware.kryo.ObjectBuffer;

/**
 * @author armin
 *
 */
public class Elagabalus {

	private final static long MAX_FILE_SIZE = Long.MAX_VALUE;
	
	private boolean autoPersist;
	private long fileSize;
	//private File file;
	private FileChannel fileChannel = null;
	private State state;
	private Kryo kryo;
	
	public Elagabalus(String filePath) throws IOException {		
		this.autoPersist = false;
		this.fileSize = 1024;
		this.kryo = new Kryo();
		this.fileChannel = this.initializeFile(filePath, this.fileSize);
		if(this.state == null) {
			this.state = new State(1024);
			this.writeState();
		}
	}

	public Elagabalus(String filePath, long fileSize) {
		if(fileSize > MAX_FILE_SIZE) {
			Log.error("File size is too large.  Cannot create file.");
			System.exit(0);
		}
		this.fileSize = fileSize;
		this.fileChannel = this.initializeFile(filePath, this.fileSize);
		if(this.state == null) {
			this.state = new State(fileSize);
		}
	}
	
	public void write(String id, byte[] data) throws IOException {
		if(this.state.getEntries().containsKey(id)) {
			System.out.println("The key already exists in the state!");
			return;
		}
		long beginByte = this.state.getEntryEnd() + 1;
		long endByte = beginByte + (long)data.length;
		Entry newEntry = new Entry(beginByte, endByte, id);
		this.state.getEntries().put(id, newEntry);
		this.state.setEntryEnd(beginByte);
		writeByteBuffer(ByteBuffer.wrap(data), beginByte, endByte);
	}
	
	public byte[] read(String id) throws IOException {
		Entry entry = this.state.getEntries().get(id);
		int bufferSize = safeLongToInt((entry.getEndPos() - entry.getBeginPos()));
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		this.fileChannel.position(entry.getBeginPos());
		this.fileChannel.read(buffer);
		return buffer.array();
	}
	
	public boolean remove(String id) {
		return false;
	}
	
	public void save() throws IOException {
		this.writeState();
		this.flush();
	}
	
	public void flush() {
		
	}
	
	public void close() {
		
	}
	
	public byte[] objectToBytes(Object object) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Output output = new Output(outputStream);
		this.kryo.writeClassAndObject(output, this.state);
		System.out.println("Buffer " + output.getBuffer().toString());
		output.close();
		byte[] buffer = outputStream.toByteArray();
		return buffer;
	}
	
	//private long[] writeBytes(String id, byte[] data) {
	//return null;
	//}
	
	private void writeByteBuffer(ByteBuffer data, long begin, long end) throws IOException {
		this.fileChannel.position(begin);
		this.fileChannel.write(data);
	}
	
	private FileChannel initializeFile(String filePath, long fileSize) {
		/* Check to see if the file exists.  Verify that it is a valid
         * PojoStick object store, and then do some further validations
         * to make sure that it is useable.  If it doesn't exist, create
         * it.
         */
		File newFile = new File(filePath);
        if (newFile.exists()) {
            if (newFile.isDirectory()) {
                Log.error("File is a directory and cannot be used.");
                System.exit(0);
            }
            if (!newFile.canRead() || !newFile.canWrite()) {
                Log.error("PojoStick does not have full permissions to use this file.");
                System.exit(0);
            }
            try {
                boolean verified = this.verifyFile();
            } catch (Exception e) {
            	Log.error("File " + e + " exists, but was not verifiable as a valid Elagalus file.");
            	System.exit(0);
            }
        } else {
            try {
                File parent = newFile.getParentFile();
                //System.out.println("Parent: " + parent);
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
            } catch (Exception ex) {
                Log.error("Exception " + ex);
            }
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(newFile, "rw");
            randomAccessFile.setLength(fileSize);
        } catch (IOException e1) {
            Log.error("Unable to create random access file.");
        }

        FileChannel outputFileChannel = null;
        outputFileChannel = randomAccessFile.getChannel();
        return outputFileChannel;
    }
	
	private State readState() {
		// TODO Auto-generated method stub
		//Kryo kryo = new Kryo();
		State state = null;
		try {
			ByteBuffer buffer = ByteBuffer.allocateDirect(16);
			this.fileChannel.position(0);
			this.fileChannel.read(buffer);
			long beginning = buffer.getLong(0);
			long end = buffer.getLong(9);
			int length =  safeLongToInt(end - beginning);
			ByteBuffer stateBuffer = ByteBuffer.allocateDirect(length);
			this.fileChannel.position(beginning);
			this.fileChannel.read(stateBuffer);
			Input input = new Input(new ByteBufferInputStream(stateBuffer));
			state = this.kryo.readObject(input, State.class);
			input.close();
			return state;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.warn("Unable to extract state class from file.");	
		}
		return state;
	}

	private void writeState() throws IOException {
		Kryo kryo = new Kryo();
		//ByteBuffer stateBuffer = ByteBuffer.allocate(capacity)
		//ByteBufferOutputStream outputStream =  new ByteBufferOutputStream();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		//ObjectBuffer ob = new ObjectBuffer(kryo);
		Output output = new Output(outputStream);
		//Output output = new ByteBufferOutput(outputStream);
		kryo.writeClassAndObject(output, this.state);
		System.out.println("Buffer " + output.getBuffer().toString());
		output.close();
		//ByteBuffer buffer = outputStream.getByteBuffer();
		byte[] buffer = outputStream.toByteArray();
		//long[] pos = this.writeBytes(this.state.getId(), buffer);
		long stateSize = (long)buffer.length;
		System.out.println("State is " + stateSize);
		long stateEnd = 16 + stateSize;
		//ByteBuffer writeBuffer = ByteBuffer.allocate(16 + buffer.length);
		byte[] beginByte = ByteBuffer.allocate(8).putLong(0).array();
		byte[] endByte = ByteBuffer.allocate(8).putLong(stateEnd).array();
		ByteArrayOutputStream writeStream = new ByteArrayOutputStream();
		writeStream.write(beginByte);
		writeStream.write(endByte);
		writeStream.write(buffer);
		byte[] toWrite = writeStream.toByteArray();
		ByteBuffer writeBuffer = ByteBuffer.wrap(toWrite);
		System.out.println("Writing...");
		this.fileChannel.position(0);
		this.fileChannel.write(writeBuffer);
		//this.fileChannel.close();
		System.out.println("Writing state finished!");
		//ByteBuffer positions = ByteBuffer.allocateDirect(16);
		//positions.putLong(0, pos[0]);
		//positions.putLong(9, pos[1]);
		//this.fileChannel.position(0);
		//this.fileChannel.write(positions);
	}
	
	private boolean verifyFile() {
		State state = this.readState();
		return true;
	}
	
	private static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
	
}
