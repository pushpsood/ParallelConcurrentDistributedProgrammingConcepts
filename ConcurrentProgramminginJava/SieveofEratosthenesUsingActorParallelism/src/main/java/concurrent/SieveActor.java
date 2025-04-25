package concurrent;

import edu.rice.pcdp.Actor;

import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 */
public final class SieveActor extends Sieve {
    /**
     * {@inheritDoc}
     *
     * Used the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. The Sieve of Eratosthenes is modeled as a pipeline
     * of actors, each corresponding to a single prime number.
     */
    @Override
    public int countPrimes(final int limit) {
        final SieveActorActor sieveActorActor = new SieveActorActor(2);
        finish(() -> {
            for (int num = 3; num <= limit; num += 2) {
                sieveActorActor.send(num);
            }
            sieveActorActor.send(0); // Signal the end of input
        });

        // Collect all primes from the actor chain
        int numPrimes = 0;
        SieveActorActor currentActor = sieveActorActor;
        while (currentActor != null) {
            numPrimes++;
            currentActor = currentActor.nextActor;
        }
        return numPrimes;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in parallel.
     * A full description of the Actor APIs can be found in the PCDP Javadocs at
     * <a href="https://habanero-rice.github.io/PCDP/">https://habanero-rice.github.io/PCDP/</a>
     */
    public static final class SieveActorActor extends Actor {
        private final int prime;
        private SieveActorActor nextActor;

        SieveActorActor(final int prime) {
            this.prime = prime;
            this.nextActor = null;
        }

        /**
         * Process a single message sent to this actor.
         *
         * @param msg Received message
         */
        @Override
        public void process(final Object msg) {
            final int candidate = (Integer) msg;
            if (candidate <= 0) {
                if (nextActor != null) {
                    nextActor.send(candidate);
                }
                return;
            }

            if (candidate % prime != 0) {
                if (nextActor == null) {
                    nextActor = new SieveActorActor(candidate);
                } else {
                    nextActor.send(candidate);
                }
            }
        }
    }
}