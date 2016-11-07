package synchronizers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

interface Worker extends Runnable{
	public int [] get_result();
}

class Matrix{
	int [][]matrix_a;
	int [][]matrix_b;
	int [][]result;
	CyclicBarrier barrier;
	Worker row_0;
	Worker row_1;
	
	Matrix(){	
		this.barrier = new CyclicBarrier(3,new ConsolidateAction() );
		
	}
	int[][] add(int [][]matrix_a,int [][]matrix_b){
		this.matrix_a = matrix_a;
		this.matrix_b = matrix_b;
		this.result = new int[matrix_a.length][matrix_a[0].length];
		row_0 = new AddWorker(0,barrier);
		row_1 = new AddWorker(1,barrier);
		
		new Thread(row_0).start();
		new Thread(row_1).start();
		
		try {
			System.out.println("Waiting for Matrix Addition");
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	int[][] sub(int [][]matrix_a,int [][]matrix_b){
		this.matrix_a = matrix_a;
		this.matrix_b = matrix_b;
		this.result = new int[matrix_a.length][matrix_a[0].length];
		row_0 = new SubWorker(0,barrier);
		row_1 = new SubWorker(1,barrier);
		
		new Thread(row_0).start();
		new Thread(row_1).start();
		
		try {
			System.out.println("Waiting for Matrix Subtraction");
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		return result;
	}
	class ConsolidateAction implements Runnable{
		@Override
		public void run() {
			int [] res_0 = row_0.get_result();
			int [] res_1 = row_1.get_result();
			
			for(int i =0;i<res_0.length;i++){
				result[0][i] = res_0[i];
				result[1][i] = res_1[i];
			}			
		}
	}
	
	
	class AddWorker implements Worker{
		int []row_a;
		int []row_b;
		int row_number;
		int [] res;
		CyclicBarrier barrier;
		
		AddWorker(int row_number,CyclicBarrier barrier){
			this.row_number = row_number;
			this.row_a = matrix_a[row_number];
			this.row_b = matrix_b[row_number];
			this.barrier = barrier;		
		}

		@Override
		public void run() {
			res = new int[row_a.length];
			for (int i =0;i<row_a.length;i++){
				res[i] = row_a[i] + row_b[i];
			}
			try {
				Thread.sleep(1000);
				System.out.println("Completed for Row  "+row_number );
				this.barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		public int [] get_result(){
			return res;
		}
	
	}
	

class SubWorker implements Worker{
	int []row_a;
	int []row_b;
	int row_number;
	int [] res;
	CyclicBarrier barrier;
	
	SubWorker(int row_number,CyclicBarrier barrier){
		this.row_number = row_number;
		this.row_a = matrix_a[row_number];
		this.row_b = matrix_b[row_number];
		this.barrier = barrier;
		
	}

	@Override
	public void run() {
		res = new int[row_a.length];
		for (int i =0;i<row_a.length;i++){
			res[i] = row_a[i] - row_b[i];
		}
		try {
			Thread.sleep(1000*5);
			System.out.println("Completed for Row  "+row_number );
			this.barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int [] get_result(){
		return res;
	}
	
}

}


public class CyclicBarrierDemo {
	public static void main(String []args){
		
		int [][]matrix_a = {{1,1},{1,1}};
		int [][]matrix_b = {{2,2},{3,2}};
		
		Matrix _matrix = new Matrix();
		int [][]result =_matrix.add(matrix_a,matrix_b);
		printMatrix(result);
		
		//Reuse Cyclic Barrier for Subtraction
		
		result =_matrix.sub(matrix_a,matrix_b);
		printMatrix(result);
							
		
	}
	
	public static void printMatrix(int [][]x){
		for (int []res: x){
			for (int val: res){
				System.out.print(val+", ");
			}
			System.out.println(" ");
		}
	}

}
