/**
 * 
 */
package org.arminhammer.elagabalus;

/**
 * @author armin
 *
 */
public class Elagabalus {

	private final static long MAX_FILE_SIZE = 4294967296;
	
	private boolean autoPersist;
	private long fileSize;
	private String filePath;
	
	public Elagabalus(String filePath) {		
		this.autoPersist = false;
		this.fileSize = 100;
		this.filePath = filePath;
	}
	
	public Elagabalus(String filePath, long fileSize) {
		this.fileSize = fileSize;
		this.filePath = filePath;
	}
	
	public int[] write(String id, byte[] data) {
		return null;
	}
	
	public byte[] read(String id) {
		return null;
	}
	
	public void save() {
		
	}
	
}
