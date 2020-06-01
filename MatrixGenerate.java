import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;

/** @author Alvin Grissom II
 * Example code for generating random matrices and timing the generation.
 **/

public class MatrixGenerate implements Runnable {
    final int[][] matrix;
    final int i, j;


    public static void main(String[] args) {
	//demo();
	//System.exit(0);
	
	if(args.length < 2) {
	    System.err.println("Usage: java MatrixGenerate [numRows] [numCols]");
	    System.exit(1);
	}
	int[][] matrix;
	int numRows = Integer.parseInt(args[0]);
	int numCols = Integer.parseInt(args[1]);
	long start = System.currentTimeMillis();
	matrix = generateRandomMatrixThreaded(numRows, numCols);
	long end = System.currentTimeMillis();
 	System.err.println("Generating with threads took " + ((float)(end - start) / 1000F) + " seconds.");

	start = System.currentTimeMillis();
	matrix  = generateRandomMatrix(numRows, numCols);
	end = System.currentTimeMillis();
	System.err.println("Generating with one thread took " + ((float)(end - start) / 1000F) + " seconds.");

	//printMatrix(matrix);
	

	
    }
    
    /**
     * Constructor that sets the internal variables for the matrix to be generated
     * @param matrix a 2D array, representing a matrix
     * @param i number of rows
     * @param j number of columns
     **/
    public MatrixGenerate(final int[][] matrix, final int i, final int j) {
		this.i = i;
		this.j = j;
		this.matrix = matrix;
    }

    /**
     * Prints matrix to the screen
     * @param matrix the matrix to be printed
     **/
    public static void printMatrix(final int[][] matrix) {
	for(int i = 0; i < matrix.length; i++) {
	    System.out.println(Arrays.toString(matrix[i]));
	}
	System.out.println();
    }

    /**
     * Demonstrates generation and multiplication of matrices.
     **/
    /*
    public static void demo() {
	System.err.println("Generating matrices.");
	int[][] a = generateRandomMatrix(1000, 1000);
	int[][] b = generateRandomMatrix(1000, 1000);
	System.err.println("Multiplying matrices.");
	int[][] c = MatrixMultiply.multiplyThreaded(a, b);
	printMatrix(c);

    }
*/
    /**
     * Generates random matrix and returns it
     * @param rows number of rows
     * @param cols number of columns
     * @return the matrix generated
     **/
    public static final int[][] generateRandomMatrix(final int rows, final int cols) {
	int[][] matrix = new int[rows][cols];
	for(int i = 0; i < rows; i++) {
	    for(int j = 0; j < cols; j++) {
		matrix[i][j] = (int)(Math.random() * 100F);
	    }
	}
	return matrix;
    }

    /**
     * Generates random matrix with multiple threads and returns it
     * @param rows number of rows
     * @param cols number of columns
     * @return the matrix generated
     **/
    public static final int[][] generateRandomMatrixThreaded(final int rows, final int cols) {

	int[][] newMatrix = new int[rows][cols];
	//ExecutorService executor = Executors.newWorkStealingPool(4);
	ExecutorService executor = Executors.newCachedThreadPool();
	//ExecutorService executor = Executors.newFixedThreadPool(4);
	
	
	//for each row in a
	for(int i = 0; i < rows; i++) {
	    //for each column in b
	    for(int j = 0; j < cols; j++) {
		Runnable thread = new MatrixGenerate(newMatrix, i, j);
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
     * Generates random number at index (i,j) aand sets it in the matrix inside of a thread.
     */
    public void run() {
	this.matrix[i][j] = (int)(Math.random() * 100F);
    }

    
}
