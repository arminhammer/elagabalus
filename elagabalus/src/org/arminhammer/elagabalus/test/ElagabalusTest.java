/**
 * 
 */
package org.arminhammer.elagabalus.test;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.arminhammer.elagabalus.Elagabalus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author armin
 * 
 */
public class ElagabalusTest {

	Random rand;
	int min, max;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		min = 1;
		max = 100000;
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTenStrings() {
		System.out.println("Testing with 10 strings");
		HashMap<String, String> originalHash = getStringHash(10, min, max);
		Set<String> keys = originalHash.keySet();
		String[] keyArray = (String[])keys.toArray();
		Elagabalus store = new Elagabalus("/home/armin/test/test.ela");
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < keyArray.length; i++) {
			String key = keyArray[i];
			String value = originalHash.get(key);
			store.write(key, value.getBytes());
		}
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Time to add elements: " + elapsedTime);
		
		startTime = System.currentTimeMillis();
		HashMap<String, String> readHash = new HashMap<String, String>();
		for (int i = 0; i < keyArray.length; i++) {
			String key = keyArray[i];
			String value = store.read(key).toString();
			readHash.put(key, value);
		}
		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;
		System.out.println("Time to read elements: " + elapsedTime);
		assertTrue(originalHash.equals(readHash));
	}
	/**
	 * Test method for
	 * {@link org.arminhammer.elagabalus.Elagabalus#Elagabalus(java.lang.String)}
	 * .
	 */
	// @Test
	public final void testElagabalusString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.arminhammer.elagabalus.Elagabalus#Elagabalus(java.lang.String, long)}
	 * .
	 */
	// @Test
	public final void testElagabalusStringLong() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.arminhammer.elagabalus.Elagabalus#write(java.lang.String, byte[])}
	 * .
	 */
	// @Test
	public final void testWrite() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.arminhammer.elagabalus.Elagabalus#read(java.lang.String)}.
	 */
	// @Test
	public final void testRead() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.arminhammer.elagabalus.Elagabalus#remove(java.lang.String)}.
	 */
	// @Test
	public final void testRemove() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arminhammer.elagabalus.Elagabalus#save()}.
	 */
	//@Test
	public final void testSave() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arminhammer.elagabalus.Elagabalus#close()}.
	 */
	//@Test
	public final void testClose() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.arminhammer.elagabalus.Elagabalus#safeLongToInt(long)}.
	 */
	//@Test
	public final void testSafeLongToInt() {
		fail("Not yet implemented"); // TODO
	}

	private static ArrayDeque<String> generateRandStrings(int count, int min, int max) {
		Random rand = new Random();
		ArrayDeque<String> queue = new ArrayDeque<String>();
		for (int i = 0; i < count; i++) {
			int randomNum = rand.nextInt(max - min + 1) + min;
			String string = RandomStringUtils.random(randomNum);
			queue.push(string);
		}
		return queue;
	}

	private static ArrayDeque<String> generateIDs(int count, int min, int max) {
		Random rand = new Random();
		ArrayDeque<String> queue = new ArrayDeque<String>();
		for (int i = 0; i < count; i++) {
			int randomNum = rand.nextInt(max - min + 1) + min;
			String uuid = UUID.randomUUID().toString();
			queue.push(uuid);
		}
		return queue;
	}

	private static HashMap<String, String> getStringHash(int count, int min, int max) {
		Random rand = new Random();
		HashMap<String, String> hash = new HashMap<String, String>();
		ArrayDeque<String> ids = generateIDs(count, min, max);
		ArrayDeque<String> strings = generateRandStrings(count, min, max);
		for (int i = 0; i < count; i++) {
			hash.put(ids.pop(), strings.pop());
		}
		return hash;
	}

}
