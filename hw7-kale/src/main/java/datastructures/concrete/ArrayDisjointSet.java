package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IList;

/**
 * @see IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;
    private IDictionary<T, Integer> setDic;
    private int current;
    private int pointerSize = 10;

    // However, feel free to add more fields and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    public ArrayDisjointSet() {
        // throw new NotYetImplementedException();
        this.pointers = new int[pointerSize];
        this.setDic = new ChainedHashDictionary<>();
        this.current = 0;
    }

    @Override
    public void makeSet(T item) {
        // throw new NotYetImplementedException();
        if (this.setDic.containsKey(item)) {
            throw new IllegalArgumentException();
        } else {
            this.setDic.put(item, current);
            this.pointers[current] = -1;
            this.current++;
            if (this.current == this.pointerSize) {
                int[] temp = new int[this.pointerSize*2];
                int tempIndex = 0;
                for (int i : this.pointers) {
                    temp[tempIndex] = i;
                    tempIndex++;
                }
                this.pointers = temp;
                this.pointerSize = this.pointerSize*2;
            }
        }
    }

    @Override
    public int findSet(T item) {
        // throw new NotYetImplementedException();
        if (!this.setDic.containsKey(item)) {
            throw new IllegalArgumentException();
        } else {
            IList<Integer> pathIndex = new DoubleLinkedList<>();
            int itemIndex = this.setDic.get(item);
            pathIndex.add(itemIndex);
            int parentIndex = this.pointers[itemIndex];
            while (parentIndex>=0) {
                itemIndex = parentIndex;
                pathIndex.add(itemIndex);
                parentIndex = this.pointers[itemIndex];
            }
            for (int i=0; i<pathIndex.size()-1; i++) {
                this.pointers[pathIndex.get(i)] = itemIndex;
            }
            return itemIndex;
        }
    }

    @Override
    public void union(T item1, T item2) {
        //throw new NotYetImplementedException();
        if (!this.setDic.containsKey(item1) || !this.setDic.containsKey(item2)){
            throw new IllegalArgumentException();
        } else {
            int root1 = findSet(item1);
            int root2 = findSet(item2);
            if (root1 != root2) {
                if (this.pointers[root1] == this.pointers[root2]) {
                    this.pointers[root1] = this.pointers[root1] - 1;
                    this.pointers[root2] = root1;
                } else if (this.pointers[root1] < this.pointers[root2]) {
                    this.pointers[root2] = root1;
                } else {
                    this.pointers[root1] = root2;
                }
            }
        }
    }
}
