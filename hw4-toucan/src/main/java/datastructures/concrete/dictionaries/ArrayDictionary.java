package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field.
    // We will be inspecting it in our private tests.
    private Pair<K, V>[] pairs;
    private int size;

    // You may add extra fields or helper methods though!

    public ArrayDictionary() {
        this.pairs = makeArrayOfPairs(10);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        int index = this.indexOf(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        } else {
            return this.pairs[index].value;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = this.indexOf(key);
        if (index != -1) {
            this.pairs[index].value = value;
        } else {
            if (this.size == this.pairs.length) {
                int oldSize = this.pairs.length;
                int newSize = oldSize*2;
                Pair<K, V>[] newPairs = this.makeArrayOfPairs(newSize);
                for (int i=0; i<oldSize; i++) {
                    newPairs[i] = this.pairs[i];
                }
                this.pairs = newPairs;
            }
            Pair<K, V> newPairElement = new Pair<K, V>(key, value);
            this.pairs[this.size] = newPairElement;
            size++;
        }
    }

    @Override
    public V remove(K key) {
        int index = this.indexOf(key);
        V value;
        if (index == -1) {
            throw new NoSuchKeyException();
        } else {
            value = this.pairs[index].value;
            this.pairs[index] = this.pairs[this.size-1];
            size--;
        }
        return value;
    }

    @Override
    public boolean containsKey(K key) {
       return this.indexOf(key) != -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    /** return index else return -1;
    */
    public int indexOf(K key) {
        int index = 0;
        while (index < this.size) {
            if (this.pairs[index].key == null) {
                if (this.pairs[index].key == key) {
                    return index;
                } else {
                    index++;
                }
            } else if (this.pairs[index].key.equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        return -1;
    }
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<>(this.pairs, this.size);
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] pairs;
        private int size;
        private int i;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.pairs = pairs;
            this.size = size;
            this.i = 0;
        }

        public boolean hasNext() {
            return i <= size - 1;
        }

        public KVPair<K, V> next() {
            if (this.hasNext()) {
                KVPair<K, V> currPair = new KVPair<K, V>(this.pairs[i].key, this.pairs[i].value);
                i++;
                return currPair;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

}



