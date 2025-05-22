package concurrent.boruvka.parallel;

import concurrent.AbstractBoruvka;
import concurrent.SolutionToBoruvka;
import concurrent.boruvka.Edge;

import java.util.Queue;

/**
 * A parallel implementation of Boruvka's algorithm to compute a Minimum
 * Spanning Tree.
 */
public final class ParBoruvka extends AbstractBoruvka<ParComponent> {

    /**
     * Constructor.
     */
    public ParBoruvka() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void computeBoruvka(final Queue<ParComponent> nodesLoaded, final SolutionToBoruvka<ParComponent> solution) {
        ParComponent node;
        while ((node = nodesLoaded.poll()) != null) {
            if (!node.lock.tryLock()) {
                continue;
            }

            if (node.isDead) {
                node.lock.unlock();
                continue;
            }

            Edge<ParComponent> edge = node.getMinEdge();
            if (edge == null) {
                solution.setSolution(node);
                break;
            }

            ParComponent other = edge.getOther(node);
            if (!other.lock.tryLock()) {
                node.lock.unlock();
                nodesLoaded.add(node);
                continue;
            }

            if (other.isDead) {
                node.lock.unlock();
                other.lock.unlock();
                nodesLoaded.add(node);
                continue;
            }

            other.isDead = true;
            node.merge(other, edge.weight());

            node.lock.unlock();
            other.lock.unlock();

            nodesLoaded.add(node);
        }
    }
}
