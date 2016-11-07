package queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueDemo {

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<String> queue = new PriorityBlockingQueue<String>();
		
		ExecutorService service = Executors.newCachedThreadPool();
		
		service.execute(new Producer(queue));
		Thread.sleep(5000);
		service.execute(new Consumer(queue));
		
		service.shutdown();

	}

}
