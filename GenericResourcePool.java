import java.util.ArrayList;
import java.util.Random;

/**
 * @author Brian Fauble
 *
 */
public class GenericResourcePool<R> {

	private volatile boolean open;

	private volatile MyBlockingQueue<R> availableResources, unavailableResources;

	public GenericResourcePool() {
		this.open = false;
		this.availableResources = new MyBlockingQueue<R>();
		this.unavailableResources = new MyBlockingQueue<R>();
	}

	/**
	 * Open pool to allow acquisition of resources.
	 */

	public synchronized void open() {
		this.open = true;
	}

	public synchronized boolean isOpen() {
		return this.open;
	}

	/**
	 * Close pool for acquisition. Block if resources are currently checked
	 * out of pool. Close once all are returned.
	 */

	public synchronized void close() {
		while (this.unavailableResources.size() != 0) {
			//Block until checked out resources are released.
		}
		this.open = false;
	}

	/**
	 * Close pool for acquisition immediately, whether resources are checked
	 * out or not.
	 */

	public synchronized void closeNow() {
		this.open = false;
	}

	/**
	 * Add resource to pool if not currently managed.
	 * @param resource
	 * @return boolean successful addition
	 */

	public synchronized boolean add(R resource) {
		if (!this.availableResources.contains(resource) && !this.unavailableResources.contains(resource)) {
			return this.availableResources.safeAdd(resource);
		} else {
			return false;
		}
	}

	/**
	 * If resource is tracked by pool, remove from available resources once available.
	 * @param resource
	 * @return boolean successful removal
	 */

	public synchronized boolean remove(R resource) {
		if (this.availableResources.contains(resource) || this.unavailableResources.contains(resource)) {
			while (this.availableResources.safeRemove(resource)) {
				//block until successful removal. 
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * If resource is tracked by pool, remove from either available or unavailable queue.
	 * @param resource
	 * @return boolean successful removal
	 */

	public boolean removeNow(R resource) {
		if (this.availableResources.contains(resource)) {
			return this.availableResources.safeRemove(resource);
		} else if (this.unavailableResources.contains(resource)) {
			return this.unavailableResources.safeRemove(resource);
		} else {
			return false;
		}
	}

	/**
	 * If pool is open, attempt to acquire resource until one is available.
	 * @return R resource or null
	 * @throws InterruptedException
	 */

	public R acquire() throws InterruptedException {
		if (open) {
			R temp = null;
			while (temp == null) {
				temp = this.availableResources.safePoll();

			}
			this.unavailableResources.safeAdd(temp);
			return temp;
		} else {
			return null;
		}
	}

	/**
	 * If pool is open, attempt to acquire resource if available for specified amount of time
	 * interrupt on time up and return null.
	 * @param timeout
	 * @param unit
	 * @return R or null
	 * @throws InterruptedException
	 */

	public R acquire(long timeout, java.util.concurrent.TimeUnit unit) throws InterruptedException {
		if (open) {
			R temp = this.availableResources.safePoll(timeout, unit);
			if (temp != null) {
				this.unavailableResources.safeAdd(temp);
			}
			return temp;
		} else {
			return null;
		}
	}

	/**
	 * if resource is tracked by pool, move from unavailable to available queue.
	 * @param resource
	 */

	public synchronized void release(R resource) {
		if (this.unavailableResources.safeRemove(resource)) {
			this.availableResources.safeAdd(resource);
		}
	}

	/**
	 * Helper method for testing to view amount of available resources
	 * @return int available resources
	 */
	public synchronized int availableResourceCount() {
		return this.availableResources.size();
	}

	/**
	 * Helper method for testing to view sizes and content of queues.
	 * @return int total resources
	 */

	public synchronized int totalResourceCount() {
		return this.availableResources.size() + this.unavailableResources.size();
	}

	/**
	 * Below is used as brute force testing attempting to replicate fringe cases of 
	 * multiple threads trying to access the same methods of the queue which could
	 * cause race case and cause program to hang. ie. remove() and release(), acquire()
	 * and remove().
	 */

	public static void main(String[] args) throws InterruptedException {
		final GenericResourcePool<String> pool = new GenericResourcePool<String>();
		final Random rand = new Random();
		pool.open();
		pool.add(new String("bad1"));
		pool.add(new String("bad2"));
		strings.add(pool.acquire());
		strings.add(pool.acquire());

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				new Thread("open and close") {
					public void run() {
						while (true) {
							for (int i = 1;; i++) {
								if (i % 2 == 0) pool.closeNow();
								else pool.open();
							}
						}
					}
				}.start();
				for (int i = 0; i < 5; i++) {
					new Thread("good producer " + String.valueOf(i)) {
						public void run() {
							while (true) {
								try {
									pool.add(new String("yay!"));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}.start();
				}
				for (int i = 0; i < 2000; i++) {
					new Thread("badconsumer " + String.valueOf(i)) {
						public void run() {
							while (true) {
								try {
									for (int i = 1; i < 5000; i++) {
										if (i % 2 == 0) pool.release(strings.get(1));
										else pool.remove(strings.get(0));
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}.start();
					new Thread("producer " + String.valueOf(i)) {
						public void run() {
							ArrayList<String> strings = new ArrayList<String>();
							for (int i = 0; i < 500; i++) {
								strings.add(String.valueOf(i));
							}
							int i = 2;
							int j = 1;
							while (true) {
								try {
									if (i % 2 == 0) {
										if (j % 50 == 0) {
											pool.add(strings.get(rand.nextInt(500)));
											j = 1;
										} else {
											j++;
										}
										i--;
									} else {
										if (j % 40 == 0) {
											pool.remove(strings.get(rand.nextInt(500)));
										}
										i++;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}.start();
					new Thread("consumer " + String.valueOf(i)) {
						public void run() {
							ArrayList<String> strings = new ArrayList<String>();
							int i = 2;
							while (true) {
								try {
									if (i % 2 == 0) {
										try {
											strings.add(pool.acquire());
										} catch (InterruptedException | NullPointerException e) {
											System.out.println("failed acquire");
										}
										i--;
									} else {
										if (strings != null) {
											if (!strings.isEmpty()) {
												int get = strings.size() - 1;
												pool.release(strings.get(get));
												strings.remove(get);
											}
										}
										i++;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}.start();
				}
			}
		}, 10000);
	}

	static volatile ArrayList<String> strings = new ArrayList<String>();

}
