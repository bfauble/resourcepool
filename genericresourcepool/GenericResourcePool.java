package com.bfauble.genericresourcepool;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Brian Fauble
 *
 */
public class GenericResourcePool<R> {

	private volatile boolean open;

	private LinkedBlockingQueue<R> availableResources, unavailableResources;

	public GenericResourcePool() {
		this.open = false;
		this.availableResources = new LinkedBlockingQueue<R>();
		this.unavailableResources = new LinkedBlockingQueue<R>();
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
		if (!this.availableResources.contains(resource)) {
			return this.availableResources.add(resource);
		} else {
			return false;
		}
	}

	public synchronized boolean remove(R resource) {
		if (this.availableResources.contains(resource) || this.unavailableResources.contains(resource)) {
			while (this.availableResources.remove(resource)) {
				//figure out how to block rather than infinite looping.
			}
			return true;
		} else {
			return false;
		}

	}

	public synchronized boolean removeNow(R resource) {
		if (this.availableResources.contains(resource)) {
			return this.availableResources.remove(resource);
		} else if (this.unavailableResources.contains(resource)) {
			return this.unavailableResources.remove(resource);
		} else {
			return false;
		}
	}

	public synchronized R acquire() {
		if (open) {
			R temp = this.availableResources.poll();
			this.unavailableResources.add(temp);
			return temp;
		} else {
			return null;
		}
	}

	public synchronized R acquire(long timeout, java.util.concurrent.TimeUnit unit) throws InterruptedException {
		if (open) {
			R temp = this.availableResources.poll(timeout, unit);
			this.unavailableResources.add(temp);
			return temp;
		} else {
			return null;
		}
	}

	public synchronized void release(R resource) {
		if (this.unavailableResources.remove(resource)) {
			this.availableResources.add(resource);
		}
	}

	public synchronized int availableResourceCount() {
		return this.availableResources.size();
	}

	public synchronized int totalResourceCount() {
		return this.availableResources.size() + this.unavailableResources.size();
	}

}
