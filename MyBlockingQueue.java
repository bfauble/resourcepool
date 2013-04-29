import java.util.concurrent.LinkedBlockingQueue;

public class MyBlockingQueue<R> extends LinkedBlockingQueue<R> {

	public synchronized boolean safeAdd(R resource) {
		return this.add(resource);
	}

	public synchronized R safePoll() throws InterruptedException {
		return this.poll();
	}

	public synchronized R safePoll(long timeout, java.util.concurrent.TimeUnit unit) throws InterruptedException {
		return this.poll(timeout, unit);
	}

	public synchronized boolean safeRemove(R resource) {
		return this.remove(resource);
	}

}
