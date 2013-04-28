package com.bfauble.genericresourcepool;

public class GenericResourcePool<R> {

	public GenericResourcePool() {
		// TODO Auto-generated constructor stub
	}

	void open() {

	}

	boolean isOpen() {
		return true;
	}

	void close() {

	}

	void closeNow() {

	}

	boolean add(R resource) {
		return true;
	}

	boolean remove(R resource) {
		return true;
	}

	boolean removeNow(R resource) {
		return true;
	}

	R acquire() {
		return null;
	}

	R acquire(long timeout, java.util.concurrent.TimeUnit unit) {
		return null;
	}

	void release(R resource) {

	}

}
