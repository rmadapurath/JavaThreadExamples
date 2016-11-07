package synchronizers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MatrixCountDown {
	CountDownLatch latch;
	ExecutorService executorService;

	MatrixCountDown() {
		latch = new CountDownLatch(2);
		executorService = Executors.newFixedThreadPool(2);
	}

	int[][] add(int[][] matrix_a, int[][] matrix_b) {
		int[][] result = new int[matrix_a.length][matrix_a[0].length];

		class AddWorker implements Runnable {
			int row_number;

			AddWorker(int row_number) {
				this.row_number = row_number;
			}

			@Override
			public void run() {
				for (int i = 0; i < matrix_a[row_number].length; i++) {
					result[row_number][i] = matrix_a[row_number][i]
							+ matrix_b[row_number][i];
				}
				latch.countDown();
			}
		}

		executorService.submit(new AddWorker(0));
		executorService.submit(new AddWorker(1));

		try {
			System.out.println("Waiting for Matrix Addition");
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executorService.shutdown();
		return result;
	}
}

public class CountDownLatchDemo {

	public static void main(String[] args) {
		int[][] matrix_a = { { 1, 1 }, { 1, 1 } };
		int[][] matrix_b = { { 2, 2 }, { 3, 2 } };

		MatrixCountDown _matrix = new MatrixCountDown();
		int[][] result = _matrix.add(matrix_a, matrix_b);
		printMatrix(result);

	}

	public static void printMatrix(int[][] x) {
		for (int[] res : x) {
			for (int val : res) {
				System.out.print(val + ", ");
			}
			System.out.println(" ");
		}
	}

}
