package parallel;

import junit.framework.TestCase;

/**
 * Test class for OneDimAveragingPhaser.
 */
public class OneDimAveragingPhaserTest extends TestCase {
    // Number of times to repeat each test, for consistent timing results.
    final static private int niterations = 40000;

    /**
     * Gets the number of available cores.
     * @return the number of available cores
     */
    private static int getNCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Creates an array with a specific pattern.
     * @param N The size of the array
     * @param iterations The number of iterations
     * @return The created array
     */
    private double[] createArray(final int N, final int iterations) {
        final double[] input = new double[N + 2];
        int index = N + 1;
        while (index > 0) {
            input[index] = 1.0;
            index -= (iterations / 4);
        }
        return input;
    }

    /**
     * A helper function for tests of the two-task parallel implementation.
     *
     * @param N The size of the array to test
     * @param ntasks The number of tasks
     * @return The speedup achieved, not all tests use this information
     */
    private double parTestHelper(final int N, final int ntasks) {
        // Create a random input
        double[] myNew = createArray(N, niterations);
        double[] myVal = createArray(N, niterations);
        final double[] myNewRef = createArray(N, niterations);
        final double[] myValRef = createArray(N, niterations);

        long barrierTotalTime = 0;
        long fuzzyTotalTime = 0;

        for (int r = 0; r < 3; r++) {
            final long barrierStartTime = System.currentTimeMillis();
            OneDimAveragingPhaser.runParallelBarrier(niterations, myNew, myVal, N, ntasks);
            final long barrierEndTime = System.currentTimeMillis();

            final long fuzzyStartTime = System.currentTimeMillis();
            OneDimAveragingPhaser.runParallelFuzzyBarrier(niterations, myNewRef, myValRef, N, ntasks);
            final long fuzzyEndTime = System.currentTimeMillis();

            barrierTotalTime += (barrierEndTime - barrierStartTime);
            fuzzyTotalTime += (fuzzyEndTime - fuzzyStartTime);
        }

        return (double)barrierTotalTime / (double)fuzzyTotalTime;
    }

    /**
     * Test on large input.
     */
    public void testFuzzyBarrier() {
        final double expected = 1.05;
        final double speedup = parTestHelper(2 * 1024 * 1024, getNCores() * 1);
        final String errMsg = String.format("It was expected that the fuzzy barrier parallel implementation would " +
                "run %fx faster than the barrier implementation, but it only achieved %fx speedup", expected, speedup);
        assertTrue(errMsg, speedup >= expected);
        final String successMsg = String.format("Fuzzy barrier parallel implementation " +
                "ran %fx faster than the barrier implementation", speedup);
        System.out.println(successMsg);
    }
}