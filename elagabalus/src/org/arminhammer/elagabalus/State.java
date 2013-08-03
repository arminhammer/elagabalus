/**
 * 
 */
package org.arminhammer.elagabalus;

import java.util.List;

/**
 * @author armin
 *
 */
public class State {
	
	private String id;
	private List<Entry> entries;
	public State() {
		id = Util.getUUID();
	}

	public String getId() {
		return id;
	}
	
}
