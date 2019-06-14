package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    private final double lambda;

    // You MUST use this field to store the contents of your dictionary.
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    private int chainsSize;

    public ChainedHashDictionary() {
        this.chains= makeArrayOfChains(10);
        this.chainsSize = 0;
        this.lambda=0.75;

    }

    public ChainedHashDictionary(double lambda) {
        this.lambda = lambda;
        this.chains= makeArrayOfChains(10);
        this.chainsSize = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int arraySize) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[arraySize];
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        int hashCode = setHashCode(key);
        boolean judge = chains[hashCode] == null || !chains[hashCode].containsKey(key);
        return  judge ? defaultValue : chains[hashCode].get(key);
    }

    @Override
    public V get(K key) {
        int hashCode = setHashCode(key);
        if (chains[hashCode] == null || !chains[hashCode].containsKey(key)) {
            throw new NoSuchKeyException();
        }
        return chains[hashCode].get(key);
    }

    @Override
    public void put(K key, V value) {
        int hashCode = setHashCode(key);
        if (chains[hashCode] == null) {
            chains[hashCode] = new ArrayDictionary<>();
        }
        int oldSize = chains[hashCode].size();
        chains[hashCode].put(key, value);
        this.chainsSize += (chains[hashCode].size() - oldSize);

        if ((double) this.chainsSize / (double) chains.length > this.lambda) {
            IDictionary<K, V>[] resizeChains = makeArrayOfChains(this.chainsSize*2);
            for (KVPair<K, V> pair:this) {
                int resizeHash = Math.abs(pair.getKey().hashCode()) % resizeChains.length;
                if (resizeChains[resizeHash] == null) {
                    resizeChains[resizeHash] = new ArrayDictionary<>();
                }
                resizeChains[resizeHash].put(pair.getKey(), pair.getValue());
            }
            chains = resizeChains;
        }

    }

    @Override
    public V remove(K key) {
        int hashCode = setHashCode(key);
        if (chains[hashCode] == null || !chains[hashCode].containsKey(key)){
            throw new NoSuchKeyException();
        } else {
            this.chainsSize--;
            return chains[hashCode].remove(key);
        }
    }

    @Override
    public boolean containsKey(K key) {
        int hashCode = setHashCode(key);
        if (chains[hashCode] == null){
            return false;
        } else {
            return chains[hashCode].containsKey(key);
        }
    }

    @Override
    public int size() {
        return this.chainsSize;
    }

    private int setHashCode(K key){
        if (key != null) {
            return Math.abs(key.hashCode()) % chains.length;
        } else {
            return 0;
        }
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be
     *    true once the constructor is done setting up the class AND
     *    must *always* be true both before and after you call any
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int current;
        private Iterator<KVPair<K, V>> itr;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.current = 0;
            if (this.chains[current] != null) {
                this.itr = this.chains[current].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            for (int i = current; i < chains.length; i++) {
                if (itr != null) {
                    if (itr.hasNext()) {
                        return true;
                    }
                }
                if (current == chains.length - 1) {
                    return false;
                }
                this.current++;
                if (chains[current] != null) {
                    itr = chains[current].iterator();
                } else {
                    itr = null;
                }
            }
            return false;

        }


        @Override
        public KVPair<K, V> next() {
            if (this.hasNext()) {
                return itr.next();
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
