package parallel;

import java.util.Random;

import junit.framework.TestCase;


/**
 * <important> The test can fail depending on the number of cores available on the machine. </important>
 */
public class MatrixMultiplyTest extends TestCase {
    // Number of times to repeat each test, for consistent timing results.
    final static private int REPEATS = 20;

    /**
     * Create a double[] of length N to use as input for the tests.
     *
     * @param N Size of the array to create
     * @return Initialized double array of length N
     */
    private double[][] createMatrix(final int N) {
        final double[][] input = new double[N][N];
        final Random rand = new Random(314);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                input[i][j] = rand.nextInt(100);
            }
        }

        return input;
    }

    /**
     * Check if there is any difference in the correct and generated outputs.
     */
    private void checkResult(final double[][] ref, final double[][] output, final int N) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                String msg = "Error detected on cell (" + i + ", " + j + ")";
                assertEquals(msg, ref[i][j], output[i][j]);
            }
        }
    }

    /**
     * A helper function for tests of the two-task parallel implementation.
     *
     * @param N The size of the array to test
     * @return The speedup achieved, not all tests use this information
     */
    private double parTestHelper(final int N) {
        // Create a random input
        final double[][] A = createMatrix(N);
        final double[][] B = createMatrix(N);
        final double[][] C = new double[N][N];
        final double[][] refC = new double[N][N];

        // Use a reference sequential version to compute the correct result
        MatrixMultiply.seqMatrixMultiply(A, B, refC, N);

        // Use the parallel implementation to compute the result
        MatrixMultiply.parMatrixMultiply(A, B, C, N);

        checkResult(refC, C, N);

        /*
         * Run several repeats of the sequential and parallel versions to get an accurate measurement of parallel
         * performance.
         */
        final long seqStartTime = System.currentTimeMillis();
        for (int r = 0; r < REPEATS; r++) {
            MatrixMultiply.seqMatrixMultiply(A, B, C, N);
        }
        final long seqEndTime = System.currentTimeMillis();

        final long parStartTime = System.currentTimeMillis();
        for (int r = 0; r < REPEATS; r++) {
            MatrixMultiply.parMatrixMultiply(A, B, C, N);
        }
        final long parEndTime = System.currentTimeMillis();

        final long seqTime = (seqEndTime - seqStartTime) / REPEATS;
        final long parTime = (parEndTime - parStartTime) / REPEATS;
        System.out.println("seqTime: " + seqTime);
        System.out.println("parTime: " + parTime);
        int speedup = (int)(seqTime / (double)parTime);
        System.out.println("Speedup: " + speedup);
        return speedup;
    }

    /**
     * Tests the performance of the parallel implementation on a 512x512 matrix.
     */
    public void testPar512_x_512() {
        parTestHelper(512);
        // The parallel version will take more time here because of the overhead of parallelization.
    }

    /**
     * Tests the performance of the parallel implementation on a 768x768 matrix.
     */
    public void testPar768_x_768() {
        parTestHelper(768);
        // The parallel version will take more time here because of the overhead of parallelization.
    }
}
