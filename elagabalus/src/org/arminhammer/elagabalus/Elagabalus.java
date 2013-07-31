/**
 * 
 */
package org.arminhammer.elagabalus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.esotericsoftware.minlog.Log;

/**
 * @author armin
 *
 */
public class Elagabalus {

	private final static long MAX_FILE_SIZE = Long.MAX_VALUE;
	
	private boolean autoPersist;
	private long fileSize;
	private File file;
	private FileChannel fileChannel = null;
	private State state;
	
	public Elagabalus(String filePath) {		
		this.autoPersist = false;
		this.fileSize = 100;
		this.file = this.initializeFile(filePath);
		this.state = this.getState();
	}

	public Elagabalus(String filePath, long fileSize) {
		if(fileSize > MAX_FILE_SIZE) {
			Log.error("File size is too large.  Cannot create file.");
			System.exit(0);
		}
		this.fileSize = fileSize;
		this.file = this.initializeFile(filePath);
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
	
	private File initializeFile(String filePath) {
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
        return newFile;
    }
	
	private State getState() {
		// TODO Auto-generated method stub
		return null;
	}

	private void verifyFile() {
		
	}
}
