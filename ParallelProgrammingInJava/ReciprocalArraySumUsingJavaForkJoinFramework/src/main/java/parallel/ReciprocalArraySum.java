package parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


/**
 * A utility class for computing the sum of reciprocals of an array using sequential and
 * parallel programming with the Fork/Join framework.
 */
public final class ReciprocalArraySum {

    private ReciprocalArraySum() {
    }

    /**
     * Computes the sum of the reciprocals of the elements in the input array sequentially.
     * Not used anywhere, just added this to show how it is normally done without using
     * parallel programming
     *
     * @param input the array of double values
     * @return the sum of the reciprocals of the array elements
     */
    protected static double seqArraySum(final double[] input) {
        double sum = 0;
        for (double v : input) {
            sum += 1 / v;
        }
        return sum;
    }

    /**
     * Calculate the size of each chunk when dividing the work into multiple chunks.
     *
     * @param nChunks   The number of chunks to divide the work into
     * @param nElements The total number of elements to be processed
     * @return The size of each chunk
     */
    private static int getChunkSize(final int nChunks, final int nElements) {
        // Integer ceil
        return (nElements + nChunks - 1) / nChunks;
    }

    /**
     * Calculate the starting index of a chunk.
     *
     * @param chunk The chunk number
     * @param nChunks The total number of chunks
     * @param nElements The total number of elements
     * @return The starting index of the chunk
     */
    private static int getChunkStartInclusive(final int chunk, final int nChunks, final int nElements) {
        final int chunkSize = getChunkSize(nChunks, nElements);
        return chunk * chunkSize;
    }


    /**
     * Calculate the ending index of a chunk.
     *
     * @param chunk The chunk number
     * @param nChunks The total number of chunks
     * @param nElements The total number of elements
     * @return The ending index of the chunk
     */
    private static int getChunkEndExclusive(final int chunk, final int nChunks, final int nElements) {
        final int chunkSize = getChunkSize(nChunks, nElements);
        final int end = (chunk + 1) * chunkSize;
        if (end > nElements) {
            return nElements;
        } else {
            return end;
        }
    }


    /**
     * A task for computing the sum of reciprocals of an array segment using the Fork/Join framework.
     */
    private static class ReciprocalArraySumTask extends RecursiveAction {

        /** The starting index of the segment, inclusive. */
        private final int startIndexInclusive;


        /** The ending index of the segment, exclusive. */
        private final int endIndexExclusive;

        /** The input array of double values. */
        private final double[] input;

        /** The computed sum of reciprocals for the segment. */
        private double value;

        private int partition = 1;

        /**
         * Constructor.
         *
         * @param setStartIndexInclusive The starting index of the segment, inclusive.
         * @param setEndIndexExclusive The ending index of the segment, exclusive.
         * @param setInput The input array of double values.
         */
        ReciprocalArraySumTask(final int setStartIndexInclusive,
                               final int setEndIndexExclusive, final double[] setInput) {
            this.startIndexInclusive = setStartIndexInclusive;
            this.endIndexExclusive = setEndIndexExclusive;
            this.input = setInput;
            this.value = 0;
        }

        /**
         * Constructor.
         *
         * @param setStartIndexInclusive The starting index of the segment, inclusive.
         * @param setEndIndexExclusive The ending index of the segment, exclusive.
         * @param setInput The input array of double values.
         */
        ReciprocalArraySumTask(final int setStartIndexInclusive,
                               final int setEndIndexExclusive,
                               int partition,
                               final double[] setInput) {
            this.startIndexInclusive = setStartIndexInclusive;
            this.endIndexExclusive = setEndIndexExclusive;
            this.input = setInput;
            this.value = 0;
            this.partition = partition;
        }

        /**
         * Gets the computed sum of reciprocals for the segment.
         *
         * @return The sum of reciprocals.
         */
        public double getValue() {
            return value;
        }

        /**
         * The main computation performed by this task.
         * If the segment size is small enough, compute directly.
         * Otherwise, split the segment into two smaller tasks and invoke them.
         */
        @Override
        protected void compute() {
            if(partition == -1 || endIndexExclusive - startIndexInclusive <= 500000){
                for(int i=startIndexInclusive;i<endIndexExclusive;i++){
                    value += 1/input[i];
                }
            } else {
                int midPoint = (endIndexExclusive+startIndexInclusive)/2 + 1;
                ReciprocalArraySumTask reciprocalArraySumTaskLeft = new ReciprocalArraySumTask(startIndexInclusive, midPoint, input);
                ReciprocalArraySumTask reciprocalArraySumTaskRight = new ReciprocalArraySumTask(midPoint, endIndexExclusive, input);
                reciprocalArraySumTaskLeft.fork();
                reciprocalArraySumTaskRight.compute();
                reciprocalArraySumTaskLeft.join();
                value = reciprocalArraySumTaskLeft.getValue() + reciprocalArraySumTaskRight.getValue();
            }
        }
    }

    /**
     * Computes the sum of the reciprocals of the elements in the input array using parallel programming.
     *
     * @param input the array of double values
     * @return the sum of the reciprocals of the array elements
     */
    protected static double parArraySum(final double[] input) {
        assert input.length % 2 == 0;
        int start1 = getChunkStartInclusive(0,2,input.length);
        int end1 = getChunkEndExclusive(0,2,input.length);
        ReciprocalArraySumTask reciprocalArraySumTask1 = new ReciprocalArraySumTask(start1,end1,-1,input);
        reciprocalArraySumTask1.fork();
        int start2 = getChunkStartInclusive(1,2,input.length);
        int end2 = getChunkEndExclusive(1,2,input.length);
        ReciprocalArraySumTask reciprocalArraySumTask2 = new ReciprocalArraySumTask(start2,end2,-1,input);
        reciprocalArraySumTask2.compute();
        reciprocalArraySumTask1.join();
        return reciprocalArraySumTask1.getValue() + reciprocalArraySumTask2.getValue();
    }

    /**
     * Computes the sum of the reciprocals of the elements in the input array using parallel programming with numTasks tasks.
     *
     * @param input the array of double values
     * @param numTasks the number of tasks to divide the work into
     * @return the sum of the reciprocals of the array elements
     */
    protected static double parManyTaskArraySum(final double[] input, final int numTasks) {
        ForkJoinPool pool = new ForkJoinPool(numTasks);
        ReciprocalArraySumTask reciprocalArraySumTask = new ReciprocalArraySumTask(0, input.length, input);
        pool.invoke(reciprocalArraySumTask);
        return reciprocalArraySumTask.getValue();
    }
}