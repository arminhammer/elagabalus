/**
 * 
 */
package org.arminhammer.elagabalus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInputStream;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.minlog.Log;

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
	
	public Elagabalus(String filePath) {		
		this.autoPersist = false;
		this.fileSize = 100;
		this.fileChannel = this.initializeFile(filePath, this.fileSize);
		State state = this.readState();
		if(state == null) {
			state = new State();
		}
		this.state = state;
	}

	public Elagabalus(String filePath, long fileSize) {
		if(fileSize > MAX_FILE_SIZE) {
			Log.error("File size is too large.  Cannot create file.");
			System.exit(0);
		}
		this.fileSize = fileSize;
		this.fileChannel = this.initializeFile(filePath, this.fileSize);
		State state = this.readState();
		if(state == null) {
			state = new State();
		}
		this.state = state;
	}
	
	public int[] write(String id, byte[] data) {
		return null;
	}
	
	public byte[] read(String id) {
		return null;
	}
	
	public void save() {
		
	}
	
	public void close() {
		
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
                this.verifyFile();
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
                /*
                FileWriter writer;
                try {
                    writer = new FileWriter(pojofile, true);
                    writer.write(queueSeparator + "\n");
                    writer.close();
                } catch (IOException ex) {
                    Log.error("IOError: " + ex);
                }
                */
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
		State state = null;
		try {
			ByteBuffer buffer = ByteBuffer.allocateDirect(16);
			this.fileChannel.position(0);
			this.fileChannel.read(buffer);
			long beginning = buffer.getLong(0);
			long end = buffer.getLong(9);
			int length =  safeLongToInt(end - beginning);
			buffer = ByteBuffer.allocate(length);
			this.fileChannel.position(beginning);
			this.fileChannel.read(buffer);
			Input input = new Input(new ByteBufferInputStream(buffer));
			Kryo kryo = new Kryo();
			state = kryo.readObject(input, State.class);
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.warn("Unable to extract state class from file.");	
		}
		return state;
	}

	private void writeState() {
		
	}
	
	private void verifyFile() {
		
	}
	
	public static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
}
