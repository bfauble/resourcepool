//package com.bfauble.genericresourcepool;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Brian Fauble
 *
 */
public class GenericResourcePool<R> {
	static volatile ArrayList<String> stringz = new ArrayList<String>();

	private volatile boolean open;

	private volatile MyBlockingQueue<R> availableResources, unavailableResources;

	public GenericResourcePool() {
		this.open = false;
		this.availableResources = new MyBlockingQueue<R>();
		this.unavailableResources = new MyBlockingQueue<R>();
	}

	public synchronized void open() {
		this.open = true;
	}

	public synchronized boolean isOpen() {
		return this.open;
	}

	public synchronized void close() {
		while (this.unavailableResources.size() != 0) {

		}
		this.open = false;
	}

	public synchronized void closeNow() {
		this.open = false;
	}

	public synchronized boolean add(R resource) {
		if (!this.availableResources.contains(resource) && !this.unavailableResources.contains(resource)) {
			return this.availableResources.safeAdd(resource);
		} else {
			return false;
		}
	}

	public synchronized boolean remove(R resource) {
		if (this.availableResources.contains(resource) || this.unavailableResources.contains(resource)) {
			while (this.availableResources.safeRemove(resource)) {
				//figure out how to block rather than infinite looping.
			}
			return true;
		} else {
			return false;
		}

	}

	public synchronized boolean removeNow(R resource) {
		if (this.availableResources.contains(resource)) {
			return this.availableResources.safeRemove(resource);
		} else if (this.unavailableResources.contains(resource)) {
			return this.unavailableResources.safeRemove(resource);
		} else {
			return false;
		}
	}

	public R acquire() throws InterruptedException {
		if (open) {
			R temp = null;
			while (temp == null) {
				temp = this.availableResources.safePoll();

			}
			if (temp == null) System.out.println("uh oh");
			this.unavailableResources.safeAdd(temp);
			return temp;
		} else {
			return null;
		}
	}

	public synchronized R acquire(long timeout, java.util.concurrent.TimeUnit unit) throws InterruptedException {
		if (open) {
			R temp = this.availableResources.safePoll(timeout, unit);
			this.unavailableResources.safeAdd(temp);
			return temp;
		} else {
			return null;
		}
	}

	public synchronized void release(R resource) {
		if (this.unavailableResources.safeRemove(resource)) {
			this.availableResources.safeAdd(resource);
		}
	}

	public synchronized int availableResourceCount() {
		return this.availableResources.size();
	}

	public synchronized int totalResourceCount() {
		return this.availableResources.size() + this.unavailableResources.size();
	}

	public void read() {
		System.out.println(this.availableResources.toString());
		System.out.println(this.unavailableResources.toString());
	}

	public static void main(String[] args) throws InterruptedException {
		final GenericResourcePool<String> pool = new GenericResourcePool<String>();
		final Random rand = new Random();
		pool.open();
		pool.add(new String("bad1"));
		pool.add(new String("bad2"));
		stringz.add(pool.acquire());
		stringz.add(pool.acquire());

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
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
				for (int i = 0; i < 1000; i++) {

					new Thread("badconsumer " + String.valueOf(i)) {
						public void run() {
							while (true) {
								try {
									for (int i = 1; i < 10000; i++) {
										if (i % 2 == 0) pool.release(stringz.get(1));
										else pool.remove(stringz.get(0));
										//System.out.println(t_count++);
									}
									int i = 1;
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
											// TODO Auto-generated catch block
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

	//transaction count for brute force threads
	static volatile long t_count;
}
