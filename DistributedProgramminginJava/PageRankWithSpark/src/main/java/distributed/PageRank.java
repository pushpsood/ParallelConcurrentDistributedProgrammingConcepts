package distributed;

import org.apache.spark.api.java.JavaPairRDD;
import scala.Tuple2;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
/**
 * A wrapper class for the implementation of a single iteration of the iterative
 * PageRank algorithm.
 */
public final class PageRank {
    /**
     * Default constructor.
     */
    private PageRank() {
    }

    /**
     * Given an RDD of websites and their ranks, compute new ranks for all
     * websites and return a new RDD containing the updated ranks.

     * Given a website B with many other websites
     * linking to it, the updated rank for B is the sum over all source websites
     * of the rank of the source website divided by the number of outbound links
     * from the source website. This new rank is damped by multiplying it by
     * 0.85 and adding that to 0.15. Put more simply:

     *   new_rank(B) = 0.15 + 0.85 * sum(rank(A) / out_count(A)) for all A linking to B

     * For this implemented this PageRank algorithm using the Spark Java APIs.

     * The reference solution of sparkPageRank uses the following Spark RDD
     * APIs. However, you are free to develop whatever solution makes the most
     * sense to you which also demonstrates speedup on multiple threads.
     *   1) JavaPairRDD.join
     *   2) JavaRDD.flatMapToPair
     *   3) JavaPairRDD.reduceByKey
     *   4) JavaRDD.mapValues
     *
     * @param sites The connectivity of the website graph, keyed on unique
     *              website IDs.
     * @param ranks The current ranks of each website, keyed on unique website
     *              IDs.
     * @return The new ranks of the websites graph, using the PageRank
     *         algorithm to update site ranks.
     */

    public static JavaPairRDD<Integer, Double> sparkPageRank(
            final JavaPairRDD<Integer, Website> sites,
            final JavaPairRDD<Integer, Double> ranks) {

        return sites
                .join(ranks)
                .flatMapToPair(kv -> {
                    Tuple2<Website, Double> value = kv._2;

                    Website site = value._1();
                    Double currentRank = value._2();
                    Double rankSourcesRatio = currentRank / (double) site.getNEdges();

                    List<Tuple2<Integer, Double>> contrib = new LinkedList<>();

                    Iterator<Integer> iter = site.edgeIterator();

                    while (iter.hasNext()) {
                        final int target = iter.next();
                        contrib.add(new Tuple2<>(target, rankSourcesRatio));
                    }
                    return contrib.iterator();
                })
                .reduceByKey((d1, d2) -> ((Double) d1) + ((Double) d2))
                .mapValues(v -> 0.15 + 0.85 * ((Double) v));
    }
}