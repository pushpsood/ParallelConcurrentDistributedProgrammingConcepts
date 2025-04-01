package concurrent;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Wrapper class for two lock-based concurrent list implementations.
 *  * Caution:
 *  * It is generally not considered good practice to have multiple top-level classes in a single file in Java.
 *  * Each top-level class should be in its own file. This makes the code easier to read, maintain, and navigate.
 *  * However, it is acceptable to have inner classes or static nested classes within a top-level class if they are
 *  * closely related.
 */
public final class CoarseLists {
    /**
     * An implementation of the ListSet interface that uses Java locks to
     * protect against concurrent accesses.
     *
     * Implemented the add, remove, and contains methods below to support
     * correct, concurrent access to this list. Use a Java ReentrantLock object
     * to protect against those concurrent accesses. You may refer to
     * SyncList.java for help understanding the list management logic, and for
     * guidance in understanding where to place lock-based synchronization.
     */
    public static final class CoarseList extends ListSet {
        /*
         * Declared a lock for this class to be used in implementing the
         * concurrent add, remove, and contains methods below.
         */

        ReentrantLock lock = new ReentrantLock();

        /**
         * Default constructor.
         */
        public CoarseList() {
            super();
        }

        /**
         * {@inheritDoc}
         *
         * Used a lock to protect against concurrent access.
         */
        @Override
        boolean add(final Integer object) {
            lock.lock();
            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }

                if (object.equals(curr.object)) {
                    return false;
                } else {
                    final Entry entry = new Entry(object);
                    entry.next = curr;
                    pred.next = entry;
                    return true;
                }
            } finally {
                lock.unlock();
            }
        }

        /**
         * {@inheritDoc}
         *
         * Used a lock to protect against concurrent access.
         */
        @Override
        boolean remove(final Integer object) {
            lock.lock();
            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }

                if (object.equals(curr.object)) {
                    pred.next = curr.next;
                    return true;
                } else {
                    return false;
                }
            } finally {
                lock.unlock();
            }
        }

        /**
         * {@inheritDoc}
         *
         * Used a lock to protect against concurrent access.
         */
        @Override
        boolean contains(final Integer object) {
            lock.lock();
            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }
                return object.equals(curr.object);
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * An implementation of the ListSet interface that uses Java read-write
     * locks to protect against concurrent accesses.
     *
     * Implemented the add, remove, and contains methods below to support
     * correct, concurrent access to this list. Use a Java
     * ReentrantReadWriteLock object to protect against those concurrent
     * accesses. You may refer to SyncList.java for help understanding the list
     * management logic, and for guidance in understanding where to place
     * lock-based synchronization.
     */
    public static final class RWCoarseList extends ListSet {
        /*
         * Declared a read-write lock for this class to be used in
         * implementing the concurrent add, remove, and contains methods below.
         */

        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

        /**
         * Default constructor.
         */
        public RWCoarseList() {
            super();
        }

        /**
         * {@inheritDoc}
         *
         * Used a read-write lock to protect against concurrent access.
         */
        @Override
        boolean add(final Integer object) {
            rwLock.writeLock().lock();
            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }

                if (object.equals(curr.object)) {
                    return false;
                } else {
                    final Entry entry = new Entry(object);
                    entry.next = curr;
                    pred.next = entry;
                    return true;
                }
            } finally {
                rwLock.writeLock().unlock();
            }
        }

        /**
         * {@inheritDoc}
         *
         * Used a read-write lock to protect against concurrent access.
         */
        @Override
        boolean remove(final Integer object) {
            rwLock.writeLock().lock();
            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }

                if (object.equals(curr.object)) {
                    pred.next = curr.next;
                    return true;
                } else {
                    return false;
                }
            } finally {
                rwLock.writeLock().unlock();
            }
        }

        /**
         * {@inheritDoc}
         *
         * Used a read-write lock to protect against concurrent access.
         */
        @Override
        boolean contains(final Integer object) {
            rwLock.readLock().lock();
            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }
                return object.equals(curr.object);
            } finally {
                rwLock.readLock().unlock();
            }
        }
    }
}