package queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

 class Producer implements Runnable{
	BlockingQueue<String> queue;
	Producer(BlockingQueue<String> queue){
		this.queue = queue;
	}
	public void run(){
		try {
			queue.put("Hello ");
			Thread.sleep(1000);
			queue.put("Array ");
			Thread.sleep(1000);
			queue.put("Blocking Queue");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
  class Consumer implements Runnable{
		BlockingQueue<String> queue;
		Consumer(BlockingQueue<String> queue){
			this.queue = queue;
		}
		public void run(){
			try {
				System.out.println(queue.take());
				System.out.println(queue.take());
				System.out.println(queue.take());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
public class ArrayBlockingQueueDemo {
	

	public static void main(String[] args) {
	    //BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024);
		
		//unbounded queue
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		
		ExecutorService service = Executors.newCachedThreadPool();
		
		service.execute(new Producer(queue));
		service.execute(new Consumer(queue));
		
		service.shutdown();

	}

}
