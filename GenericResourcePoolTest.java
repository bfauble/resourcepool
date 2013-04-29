/**
 * 
 */
//package com.bfauble.genericresourcepool;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Brian Fauble
 *
 */
public class GenericResourcePoolTest {
	GenericResourcePool<String> testPool1, testPool2;
	String s1, s2;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testPool1 = new GenericResourcePool<String>();
		testPool2 = new GenericResourcePool<String>();
		s1 = new String("s1");
		s2 = new String("s2");
		s1 = "test1";
		s2 = "test2";
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#GenericResourcePool()}.
	 * 
	 * Test that new object created and will only allow type specified
	 * Pool is empty
	 * Acquire returns null
	 * 
	 * 
	 */
	@Test
	public void testGenericResourcePool() {

		assertTrue(testPool1.getClass() instanceof Object);
		assertFalse(testPool2.equals(testPool1));
		assertTrue(testPool1.totalResourceCount() == 0);
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#open()}.
	 * 
	 * isOpen should return false unless this has been called first.
	 */
	@Test
	public void testOpen() {
		assertFalse(testPool1.isOpen());
		testPool1.open();
		assertTrue(testPool1.isOpen());

	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#close()}.
	 * 
	 * close pool and block until all resources have been released.
	 * Create two objects, add to pool. 
	 * Acquire both objects, close pool and pool remains open until both
	 * resources have been released.
	 * @throws InterruptedException 
	 * 
	 */
	@Test
	public void testClose() throws InterruptedException {
		testPool1.open();
		assertTrue(testPool1.isOpen());
		testPool1.add(s1);
		String temp = testPool1.acquire();
		new Thread() {
			public void run() {
				testPool1.close();
				assertFalse(testPool1.isOpen());
			}
		}.start();
		assertTrue(testPool1.isOpen());
		testPool1.release(temp);
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#closeNow()}.
	 * 
	 * close pool without waiting for acquired resources to be released.
	 * Add resources, and acquire. Pool should return as close before resources
	 * have been released.
	 * @throws InterruptedException 
	 * 
	 */
	@Test
	public void testCloseNow() throws InterruptedException {
		testPool1.open();
		assertTrue(testPool1.isOpen());
		assertTrue(testPool1.add(s1));
		assertTrue(testPool1.add(s2));

		String temp1 = testPool1.acquire();
		String temp2 = testPool1.acquire();
		testPool1.closeNow();
		assertFalse(testPool1.isOpen());
		testPool1.release(temp1);
		assertTrue(testPool1.acquire() == null);

		assertFalse(testPool1.isOpen());
		testPool1.release(temp2);

		assertFalse(testPool1.isOpen());
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#add(java.lang.Object)}.
	 * 
	 * add resource to pool and add to the checked in structure.
	 * return true if new object, false if already in pool.
	 * @throws InterruptedException 
	 * 
	 */
	@Test
	public void testAdd() throws InterruptedException {
		testPool1.open();
		assertTrue(testPool1.availableResourceCount() == 0);
		assertTrue(testPool1.add(s1));
		assertFalse(testPool1.add(s1));
		assertTrue(testPool1.availableResourceCount() == 1);
		assertTrue(testPool1.add(s2));
		assertTrue(testPool1.availableResourceCount() == 2);

		String temp = testPool1.acquire();
		assertFalse(testPool1.add(temp));

	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#remove(java.lang.Object)}.
	 * 
	 * remove resource from pool from checked in structure.
	 * successful remove returns true, otherwise false.
	 * 
	 */
	@Test
	public void testRemove() {

	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#removeNow(java.lang.Object)}.
	 * 
	 * remove resource from pool from checked out structure.
	 * successful remove returns true, otherwise false.
	 * 
	 */
	@Test
	public void testRemoveNow() {

	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#acquire()}.
	 * 
	 * if pool contains resources, get one. Must block until resource is available.
	 * return null if pool is closed.
	 * @throws InterruptedException 
	 * 
	 */
	@Test
	public void testAcquire() throws InterruptedException {
		testPool1.open();

		new Thread() {
			public void run() {
				String temp = "";
				try {
					temp = testPool1.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				assertTrue(testPool1.availableResourceCount() == 0);
				testPool1.release(temp);
				testPool1.close();
				try {
					assertTrue(testPool1.acquire() == null);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		assertTrue(testPool1.availableResourceCount() == 0);
		testPool1.add(s1);
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#acquire(long, java.util.concurrent.TimeUnit)}.
	 * 
	 * attempt to acquire object for specified time, then return null if no resource returned.
	 */
	@Test
	public void testAcquireLongTimeUnit() {

	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#release(java.lang.Object)}.
	 * 
	 * return specified object to pool, remove from checked out, add to checked in.
	 * @throws InterruptedException 
	 * 
	 */
	@Test
	public void testRelease() throws InterruptedException {
		testPool1.open();
		testPool1.add(s1);
		String temp = "";
		temp = testPool1.acquire();
		assertTrue(testPool1.totalResourceCount() == 1);
		assertTrue(testPool1.availableResourceCount() == 0);
		testPool1.release(temp);
		assertTrue(testPool1.totalResourceCount() == 1);
		assertTrue(testPool1.availableResourceCount() == 1);

	}

}
