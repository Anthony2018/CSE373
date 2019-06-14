package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (isEmpty()) {
            Node<T> currN = new Node<T>(item);
            this.front=currN;
            this.back=currN;
        } else {
            Node<T> currN = new Node<T>(this.back, item, null);
            this.back.next=currN;
            this.back=currN;
        }
        size++;
    }

    @Override
    public T remove() {
        if (isEmpty()) {
            throw new EmptyContainerException();
        } else if (this.size == 1) {
            T data = this.front.data;
            this.front = null;
            this.back = null;
            this.size = 0;
            return data;
        } else {
            T data = this.back.data;
            this.back = this.back.prev;
            this.back.next=null;
            size = size-1;
            return data;
        }
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> inthemiddleNode;
            if (index <= this.size() / 2) {
                inthemiddleNode = this.front;
                for (int i = 0; i < index; i++) {
                    inthemiddleNode = inthemiddleNode.next;
                }
            } else {
                inthemiddleNode = this.back;
                for (int i = this.size - 1; i > index; i--) {
                    inthemiddleNode = inthemiddleNode.prev;
                }
            }
            return inthemiddleNode.data;
        }
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> getN = this.front;
            for (int i = 0; i < index; i++){
                getN = getN.next;
            }
            Node<T> newN = new Node<T>(getN.prev, item, getN.next);
            if (getN != this.front) {
                getN.prev.next = newN;
            } else {
                this.front = newN;
            }
            if (getN != this.back) {
                getN.next.prev = newN;
            } else {
                this.back = newN;
            }
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException();
        } else {
            if (index == 0) {
                Node<T> newN = new Node<T>(null, item, this.front);
                if (isEmpty()) {
                    this.back = newN;
                } else {
                    this.front.prev = newN;
                }
                this.front = newN;
            } else if (index == this.size) {
                Node<T> currN = new Node<T>(this.back, item, null);
                this.back.next = currN;
                this.back = currN;
            } else {
                Node<T> middleNode;
                if (index <= this.size() / 2) {
                    middleNode = this.front;
                    for (int i = 0; i < index; i++) {
                        middleNode = middleNode.next;
                    }
                } else {
                    middleNode = this.back;
                    for (int i = this.size - 1; i > index; i--) {
                        middleNode = middleNode.prev;
                    }
                }
                Node<T> nextNode = middleNode;
                Node<T> prevNode = middleNode.prev;
                Node<T> newN = new Node<T>(prevNode, item, nextNode);
                nextNode.prev = newN;
                prevNode.next = newN;
            }
        }
        size = size + 1;
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> deleteNode = this.front;
            for (int i=0; i<index; i++){
                deleteNode = deleteNode.next;
            }
            Node<T> nextnode = deleteNode.next;
            Node<T> prevnode = deleteNode.prev;
            if (deleteNode == this.front && deleteNode == this.back){
                this.front=null;
                this.back = null;
            } else if (deleteNode == this.front){
                nextnode.prev =null;
                this.front = nextnode;
            } else if (deleteNode == this.back) {
                prevnode.next = null;
                this.back = prevnode;
            } else {
                nextnode.prev = prevnode;
                prevnode.next = nextnode;
            }
            size--;
            return deleteNode.data;
        }
    }

    @Override
    public int indexOf(T item) {
        Node<T> findNode = this.front;
        int index = 0;
        while (findNode != null) {
            if (findNode.data == null) {
                if (findNode.data == item) {
                    return index;
                } else {
                    index++;
                    findNode = findNode.next;
                }
            } else if (findNode.data==item || findNode.data.equals(item)) {
                return index;
            } else {
                index++;
                findNode = findNode.next;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        return this.indexOf(other) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (current != null) {
                T data = current.data;
                current = current.next;
                return data;

            }else {
                throw new NoSuchElementException();
            }
        }
    }
}
