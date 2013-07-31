/**
 * 
 */
package org.arminhammer.elagabalus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.esotericsoftware.minlog.Log;

/**
 * @author armin
 *
 */
public class Elagabalus {

	private final static long MAX_FILE_SIZE = Long.MAX_VALUE;
	
	private boolean autoPersist;
	private long fileSize;
	private String filePath;
	
	public Elagabalus(String filePath) {		
		this.autoPersist = false;
		this.fileSize = 100;
		this.filePath = filePath;
		this.initializeFile();
	}
	
	public Elagabalus(String filePath, long fileSize) {
		if(fileSize > MAX_FILE_SIZE) {
			
		}
		this.fileSize = fileSize;
		this.filePath = filePath;
		this.initializeFile();
	}
	
	public int[] write(String id, byte[] data) {
		return null;
	}
	
	public byte[] read(String id) {
		return null;
	}
	
	public void save() {
		
	}
	
	private void initializeFile() {
		/* Check to see if the file exists.  Verify that it is a valid
         * PojoStick object store, and then do some further validations
         * to make sure that it is useable.  If it doesn't exist, create
         * it.
         */
		File testFile = new File(this.filePath);
        if (testFile.exists()) {
            try {
                this.verifyFile();
            } catch (FileNotVerifiableException e) {
                LOGGER.error("File " + e + " exists, but was not verifiable as a valid PojoStick file.");
            }
            if (this.pojofile.isDirectory()) {
                LOGGER.error("File is a directory and cannot be used.");
                System.exit(0);
            }
            if (!this.pojofile.canRead() || !this.pojofile.canWrite()) {
                LOGGER.error("PojoStick does not have full permissions to use this file.");
                System.exit(0);
            }
        } else {
            try {
                File parent = this.pojofile.getParentFile();
                //System.out.println("Parent: " + parent);
                if (!this.pojofile.getParentFile().exists()) {
                    this.pojofile.getParentFile().mkdirs();
                }
                FileWriter writer;
                try {
                    writer = new FileWriter(pojofile, true);
                    writer.write(queueSeparator + "\n");
                    writer.close();
                } catch (IOException ex) {
                    LOGGER.error("IOError: " + ex);
                }
                //System.out.println("pojofile is " + this.pojofile);
                boolean created = this.pojofile.createNewFile();
                //System.out.println(created);

            } catch (IOException ex) {
                LOGGER.error("IOException " + ex);
            }
        }
        queueThread = new QueueProcessor(this);
    }
	}

	private void verifyFile() {
		
	}
}
