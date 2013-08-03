/**
 * 
 */
package org.arminhammer.elagabalus;

/**
 * @author armin
 *
 */
public class Entry {
	private long beginPos;
	private long endPos;
	private String entryId;
	
	public Entry(long beginPos, long endPos, String entryId) {
		super();
		this.beginPos = beginPos;
		this.endPos = endPos;
		this.entryId = entryId;
	}
	
	public long getBeginPos() {
		return beginPos;
	}
	public void setBeginPos(long beginPos) {
		this.beginPos = beginPos;
	}
	public long getEndPos() {
		return endPos;
	}
	public void setEndPos(long endPos) {
		this.endPos = endPos;
	}
	public String getEntryId() {
		return entryId;
	}
	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}
	
	
}
