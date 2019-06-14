package datastructures.concrete;


import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.InvalidElementException;

/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private IDictionary<T, Integer> heapDic;
    private int size;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this.heap = makeArrayOfT(20);
        this.heapDic = new ChainedHashDictionary<>();
        this.size = 0;
        //throw new NotYetImplementedException();
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * upwards from a given index, if necessary.
     */
    private void percolateUp(int index) {
        T current = this.heap[index];
        int parentIndex = getParent(index);
        T parent = this.heap[parentIndex];
        if (great(parent, current)) {
            this.heap[parentIndex] = current;
            this.heap[index] = parent;
            this.heapDic.put(parent, index);
            this.heapDic.put(current, parentIndex);
            percolateUp(parentIndex);
        }
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * downwards from a given index, if necessary.
     */
    private void percolateDown(int index) {
        int i=1;
        int tempIndex = index;
        while (i<=4 && 4*index+i<this.size){
            if (this.heap[tempIndex].compareTo(this.heap[4*index+i]) > 0){
                tempIndex = 4*index+i;
            }
            i++;
        }
        this.swap(index, tempIndex);
        if (index != tempIndex) {
            percolateDown(tempIndex);
        }
        // throw new NotYetImplementedException();
    }

    /**
     * A method stub that you may replace with a helper method for determining
     * which direction an index needs to percolate and percolating accordingly.
     */
    private void percolate(int index) {
        if (index == 0) {
            percolateDown(index);
        }
        else {
            int parentIndex = getParent(index);
            if (great(this.heap[parentIndex], this.heap[index])) {
                percolateUp(index);
            } else if (great(this.heap[index], this.heap[parentIndex])) {
                percolateDown(index);
            }
        }
        // throw new NotYetImplementedException();
    }

    /**
     * A method stub that you may replace with a helper method for swapping
     * the elements at two indices in the 'heap' array.
     */
    private void swap(int a, int b) {
        T aItem = this.heap[a];
        T bItem = this.heap[b];
        this.heap[a] = bItem;
        this.heap[b] = aItem;
        // throw new NotYetImplementedException();
    }

    @Override
    public T removeMin() {
        if (this.size == 0) {
            throw new EmptyContainerException();
        }
        T min = this.heap[0];
        this.heap[0] = this.heap[size-1];
        this.heapDic.remove(min);
        this.size--;
        percolate(0);
        return min;
        //throw new NotYetImplementedException();
    }

    @Override
    public T peekMin() {
        if (this.size == 0) {
            throw new EmptyContainerException();
        }
        return this.heap[0];
        // throw new NotYetImplementedException();
    }

    @Override
    public void add(T item) {
        // System.out.println(this.size);
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.contains(item)) {
            throw new InvalidElementException();
        }
        if (this.size == this.heap.length) {
            int oldSize = this.heap.length;
            int newSize = oldSize*2;
            T[] newHeap = this.makeArrayOfT(newSize);
            for (int i = 0; i < oldSize; i++) {
                newHeap[i] = this.heap[i];
            }
            this.heap = newHeap;
        }
        this.heap[size] = item;
        this.heapDic.put(item, size);
        // System.out.println(this.size);
        this.size++;
        // System.out.println(this.size);
        percolate(size-1);
        // throw new NotYetImplementedException();
    }

    @Override
    public boolean contains(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        return this.heapDic.containsKey(item);
        // throw new NotYetImplementedException();
    }

    @Override
    public void remove(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.size == 0){
            throw new EmptyContainerException();
        }
        if (!this.contains(item)) {
            throw new InvalidElementException();
        }
        int indexRemoved = this.heapDic.remove(item);

        if (indexRemoved == this.size-1) {
            this.heap[indexRemoved] = null;
            this.size--;
        } else {
            T lastItem = this.heap[this.size-1];
            this.heap[indexRemoved] = lastItem;
            this.heapDic.put(lastItem, indexRemoved);
            this.heap[this.size-1] = null;
            this.size--;
            percolate(indexRemoved);
        }
        // throw new NotYetImplementedException();
    }

    @Override
    public void replace(T oldItem, T newItem) {
        if (newItem == null || oldItem == null) {
            throw new IllegalArgumentException();
        }
        if (!this.heapDic.containsKey(oldItem)) {
            throw new InvalidElementException();
        }
        if (this.heapDic.containsKey(newItem)) {
            throw new InvalidElementException();
        }
        int oldIndex = this.heapDic.get(oldItem);
        this.heap[oldIndex] = newItem;
        this.heapDic.remove(oldItem);
        this.heapDic.put(newItem, oldIndex);
        percolate(oldIndex);
        // throw new NotYetImplementedException();
    }

    @Override
    public int size() {
        return this.size;
        //throw new NotYetImplementedException();
    }

    public int getParent(int index) {
        return (index-1)/4;
    }
    /*
    public int getSmallestChildIndex(int index) {

        T child1 = this.heap[index];
        if (4*index + 1 < this.size) {
            child1 = this.heap[4 * index + 1];
        }

        T child2 = this.heap[index];
        if (4*index + 2 < this.size) {
            child2 = this.heap[4 * index + 2];
        }

        T child3 = this.heap[index];
        if (4*index + 3 < this.size) {
            child3 = this.heap[4 * index + 3];
        }

        T child4 = this.heap[index];
        if (4*index + 4 < this.size) {
            child4 = this.heap[4 * index + 4];
        }

        int smallest = index;
        if (great(this.heap[smallest], child1)) {
            smallest = 4*index + 1;
        }
        if (great(this.heap[smallest], child2)) {
            smallest = 4*index + 2;
        }
        if (great(this.heap[smallest], child3)) {
            smallest = 4*index + 3;
        }
        if (great(this.heap[smallest], child4)) {
            smallest = 4*index + 4;
        }
        return smallest;

    }*/

    public boolean great(T a, T b) {
        return a.compareTo(b) > 0;
    }

}
