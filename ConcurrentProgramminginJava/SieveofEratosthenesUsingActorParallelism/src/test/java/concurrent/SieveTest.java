package concurrent;

import junit.framework.TestCase;

/**
 * A test class for verifying the scalability and correctness of the Sieve of Eratosthenes
 * implementation using actor-based parallelism.
 */
public class SieveTest extends TestCase {
    /**
     * The expected scalability factor when doubling the number of cores.
     */
    static final double expectedScalability = 1.6;

    /**
     * Retrieves the number of available processor cores on the system.
     *
     * @return The number of available processor cores.
     */
    private static int getNCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Executes the Sieve of Eratosthenes using actor-based parallelism and verifies
     * the result against a reference implementation. Also measures the execution time.
     *
     * @param limit The upper limit for the range of numbers to find primes.
     * @param ref The reference count of primes for the given limit.
     * @return The elapsed time in milliseconds for the parallel execution.
     */
    private static long driver(final int limit, final int ref) {
        // Warmup runs to ensure consistent performance
        new SieveActor().countPrimes(limit);
        System.gc();
        new SieveActor().countPrimes(limit);
        System.gc();
        new SieveActor().countPrimes(limit);
        System.gc();

        // Measure the execution time of the parallel implementation
        final long parStart = System.currentTimeMillis();
        final int parCount = new SieveActor().countPrimes(limit);
        final long parElapsed = System.currentTimeMillis() - parStart;

        // Verify the correctness of the computed prime count
        assertEquals("Mismatch in computed number of primes for limit " + limit, ref, parCount);
        return parElapsed;
    }

    /**
     * Tests the scalability and correctness of the actor-based Sieve of Eratosthenes
     * implementation for a limit of 100,000.
     *
     * @throws InterruptedException If the test is interrupted.
     */
    public void testActorSieveOneHundredThousand() throws InterruptedException {
        final int limit = 100_000;
        final int ref = new SieveSequential().countPrimes(limit);

        long prev = -1;
        int cores = 2;
        while (cores <= getNCores()) {
            // Resize the number of worker threads to match the current core count
            edu.rice.pcdp.runtime.Runtime.resizeWorkerThreads(cores);
            final long elapsed = driver(limit, ref);

            // Verify scalability when doubling the number of cores
            if (prev > 0) {
                double scalability = (double)prev / (double)elapsed;
                assertTrue(String.format("Expected scalability of %fx going from %d cores to %d cores, but found %fx",
                        expectedScalability, cores / 2, cores, scalability), scalability >= expectedScalability);
            }

            cores *= 2;
            prev = elapsed;
        }
    }

    /**
     * Tests the scalability and correctness of the actor-based Sieve of Eratosthenes
     * implementation for a limit of 200,000.
     *
     * @throws InterruptedException If the test is interrupted.
     */
    public void testActorSieveTwoHundredThousand() throws InterruptedException {
        final int limit = 200_000;
        final int ref = new SieveSequential().countPrimes(limit);

        long prev = -1;
        int cores = 2;
        while (cores <= getNCores()) {
            // Resize the number of worker threads to match the current core count
            edu.rice.pcdp.runtime.Runtime.resizeWorkerThreads(cores);
            final long elapsed = driver(limit, ref);

            // Verify scalability when doubling the number of cores
            if (prev > 0) {
                double scalability = (double)prev / (double)elapsed;
                assertTrue(String.format("Expected scalability of %fx going from %d cores to %d cores, but found %fx",
                        expectedScalability, cores / 2, cores, scalability), scalability >= expectedScalability);
            }

            cores *= 2;
            prev = elapsed;
        }
    }
}