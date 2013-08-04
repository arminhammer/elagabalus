/**
 * 
 */
package org.arminhammer.elagabalus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author armin
 * 
 */
public class State {

	private String id;
	private HashMap<String, Entry> entries;

	public State(long fileSize) {
		id = Util.getUUID();
		entries = new LinkedHashMap<String, Entry>();
	}

	public String getId() {
		return id;
	}

	public HashMap<String, Entry> getEntries() {
		return entries;
	}

	public void setEntries(HashMap<String, Entry> entries) {
		this.entries = entries;
	}

}
