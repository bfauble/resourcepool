/**
 * 
 */
package com.bfauble.genericresourcepooltests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Brian
 *
 */
public class GenericResourcePoolTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#GenericResourcePool()}.
	 */
	@Test
	public void testGenericResourcePool() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#open()}.
	 */
	@Test
	public void testOpen() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#isOpen()}.
	 */
	@Test
	public void testIsOpen() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#close()}.
	 * 
	 * close pool and block until all resources have been released.
	 * 
	 */
	@Test
	public void testClose() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#closeNow()}.
	 * 
	 * close pool without waiting for acquired resources to be released.
	 * 
	 */
	@Test
	public void testCloseNow() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#add(java.lang.Object)}.
	 * 
	 * add resource to pool and add to the checked in structure.
	 * return true if new object, false if already in pool.
	 * 
	 */
	@Test
	public void testAdd() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#acquire()}.
	 * 
	 * if pool contains resources, get one. Must block until resource is available.
	 * return null if pool is closed.
	 * 
	 */
	@Test
	public void testAcquire() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#acquire(long, java.util.concurrent.TimeUnit)}.
	 * 
	 * attempt to acquire object for specified time, then return null if no resource returned.
	 */
	@Test
	public void testAcquireLongTimeUnit() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.bfauble.genericresourcepool.GenericResourcePool#release(java.lang.Object)}.
	 * 
	 * return specified object to pool, remove from checked out, add to checked in.
	 * 
	 */
	@Test
	public void testRelease() {
		fail("Not yet implemented");
	}

}
