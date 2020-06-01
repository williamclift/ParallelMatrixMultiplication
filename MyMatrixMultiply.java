/** ----------------------------------- 
	myMatrixMultiply.java

	@author William Clift
			Data Structures
			9 March 2020

	Compile and Run Instructions:
			
		Compile:	javac MyMatrixMultiply.java MatrixGenerate.java
						/*	Compiles the program and matrix generator. 			

			
		Run:		java MyMatrixMultiply [N] [M] [M] [P] [# of Threads]
						/*	Multiplying NxM and MxP matricies.

		Output: 
			Generating took **.*** seconds.
			Generating with threads took **.*** seconds.

    ----------------------------------- **/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;

public class MyMatrixMultiply implements Runnable{

	//private static final int index = 0;
	final int[][] matrix;
	final int[][] a;
	final int[][] b;
    final int i, j;
    static int nThreads;

	public static void main(String[] args){

		int n = Integer.parseInt(args[0]);
		int m = Integer.parseInt(args[1]);
		int p = Integer.parseInt(args[3]);
		nThreads = Integer.parseInt(args[4]);

		int cores = Runtime.getRuntime().availableProcessors();


		if(args[1].equals(args[2]) && Integer.parseInt(args[4])>0){
			int[][] matrixA = new int[n][m];
			int[][] matrixB = new int[m][p];

			MatrixGenerate matxA = new MatrixGenerate(matrixA, n, m);
			MatrixGenerate matxB = new MatrixGenerate(matrixB, m, p);

			matrixA = matxA.generateRandomMatrixThreaded(n, m);
			matrixB = matxB.generateRandomMatrixThreaded(m, p);
	
			int[][] matrix1;
			int[][] matrix2;

		/*  --Parallel vs. Single-- */
			long startm = System.currentTimeMillis();
			matrix1 = multiply(matrixA, matrixB);
			long endm = System.currentTimeMillis();
			
			long start = System.currentTimeMillis();
			matrix2 = multiplyThreaded(matrixA, matrixB);
			long end = System.currentTimeMillis();

			System.out.println("Generating took " + ((float)(endm - startm) / 1000F) + " seconds.");
			System.out.println("Generating with threads took " + ((float)(end - start) / 1000F) + " seconds.");
		 /* ------------------------  */

			//toString(matrix1);
			//toString(matrix2);
		} else{

			if(Integer.parseInt(args[4])<1){
				System.out.println("Invalid number of threads.");
			}
			else{
				System.out.println("Error in Matrix size.");
			}
			
		}

		
	}


/** ------------------------------------- 
		Naive Multiplication (Part 1)
   -------------------------------------- **/
	/**
	* The naive approach to iterating through the multi-dimensional arrays and multiplying.
	* @param a
	* @param b
	*/
	public static int[][] multiply(final int[][] a, final int[][] b){

		int n = a.length;
		int m = b.length;
		int p = b[0].length;

		int[][] product = new int[n][p];

		for(int i = 0; i < n; i++){
			for(int j = 0; j < p; j++){
				int sum = 0;
				for(int k = 0; k < m; k++){
					sum += a[i][k] * b[k][j];
				}
				product[i][j] = sum;
			}
		}

		return product;
	}



/** ------------------------------------- 
		Multi-Threaded Matrix (Part 2)
   -------------------------------------- **/

	public void run(){
		this.matrix[i][j] = dotProd(a, b, i, j);
	}


	/**
	* The multi-threaded approach to matrix multiplication
	* @param a
	* @param b
	*/
	public static int[][] multiplyThreaded(final int[][] a, final int[][] b){

		//ExecutorService executor = Executors.newWorkStealingPool(nThreads);
		ExecutorService executor = Executors.newCachedThreadPool();
		//ExecutorService executor = Executors.newFixedThreadPool(nThreads);

		int[][] newMatrix = new int[a.length][b.length];

		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < b[0].length; j++){
				Runnable thread = new MyMatrixMultiply(a, b, newMatrix, i, j); 
				executor.execute(thread);
			}
		}
		executor.shutdown();

		try { 
	    	executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch(InterruptedException e) {
		    System.err.println(e.getMessage());
		}

		
		return newMatrix;
	}

	/**
	* Completes the dot product of matricies at a particular row and column
	*	@param a
	* 	@param b
	* 	@param i
	* 	@param j
	*/
	public static int dotProd(final int[][] a,
	         				  final int[][] b,
	                          final int i,
	                          final int j){
		
		int sum = 0;
		for(int k = 0; k < b.length; k++){
			sum += a[i][k] * b[k][j];
		}

		return sum;
	}

	/**
	*	Constructor for the MyMatrixMultiply Class
	*	@param a
	* 	@param b
	* 	@param matrix
	* 	@param i
	* 	@param j
	* Constructor method for myMatrixMultiply
	*/
	public MyMatrixMultiply(final int[][] a,
	                        final int[][] b,
	                        final int[][] matrix,
	                        final int i,
	                        final int j){
		this.a = a;
		this.b = b;
		this.i = i;
		this.j = j;
		this.matrix = matrix;
	}

	/**
	*	
	* 	@param c
	*/
	public static void toString(final int[][] c){
		for(int i = 0; i < c.length; i++) {
	    	System.out.println(Arrays.toString(c[i]));
		}
		System.out.println();
	}

}