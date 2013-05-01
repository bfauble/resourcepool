import java.util.concurrent.LinkedBlockingQueue;

public class MyBlockingQueue<R> extends LinkedBlockingQueue<R> {

	/**
	 * Class provides wrapping methods for both queues to prevent race cases.
	 * Since multiple accessing methods to the resource pool use the same interface
	 * with the queue, synchronizing publicly available methods 
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected synchronized boolean safeAdd(R resource) {
		return this.add(resource);
	}

	protected synchronized R safePoll() throws InterruptedException {
		return this.poll();
	}

	protected synchronized R safePoll(long timeout, java.util.concurrent.TimeUnit unit) throws InterruptedException {
		return this.poll(timeout, unit);
	}

	protected synchronized boolean safeRemove(R resource) {
		return this.remove(resource);
	}

}
