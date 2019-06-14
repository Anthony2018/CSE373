package datastructures;

import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.InvalidElementException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.awt.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestArrayHeap extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        assertEquals(1, heap.size());
        assertFalse(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testBasicAddReflection() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        Comparable<Integer>[] array = getArray(heap);
        assertEquals(3, array[0]);
    }

    @Test(timeout=SECOND)
    public void testUpdateDecrease() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4, 5});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        IntWrapper newValue = new IntWrapper(0);
        heap.replace(values[2], newValue);

        assertEquals(newValue, heap.removeMin());
        assertEquals(values[0], heap.removeMin());
        assertEquals(values[1], heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testUpdateIncrease() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{0, 2, 4, 6, 8});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        IntWrapper newValue = new IntWrapper(5);
        heap.replace(values[0], newValue);

        assertEquals(values[1], heap.removeMin());
        assertEquals(values[2], heap.removeMin());
        assertEquals(newValue, heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());
    }

    @Test(timeout=2*SECOND)
    public void testAddAndPeekWithLargeData() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 1000; i<10000; i++) {
            heap.add(i);
            assertEquals(1000, heap.peekMin());
        }
    }

    @Test(timeout=4*SECOND)
    public void testAddAndRemoveWithLargeData() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 1000; i<10000; i++) {
            heap.add(i);
        }

        for (int i = 1000; i<10000; i++) {
            assertEquals(i, heap.removeMin());
        }
    }

    @Test(timeout=SECOND)
    public void testPeekAndRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        heap.add(3);
        heap.add(6);
        heap.add(18);
        heap.add(2);

        assertEquals(2, heap.peekMin());
        heap.add(1);
        assertEquals(1, heap.removeMin());
        assertEquals(2, heap.removeMin());
        assertEquals(3, heap.peekMin());
    }

    @Test(timeout=SECOND)
    public void testPeekAndRemoveWithEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing
        }

        try {
            heap.removeMin();
            fail("Excepted EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing
        }
    }

    @Test(timeout=SECOND)
    public void tesAddWithNullElement() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        try {
            heap.add(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing
        }
    }

    @Test(timeout=SECOND)
    public void tesAddExisted() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        heap.add(5);

        try {
            heap.add(5);
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing
        }
    }

    @Test(timeout = SECOND)
    public void testContainsAndRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        heap.add(3);
        heap.add(6);
        heap.add(18);
        heap.add(2);

        assertEquals(true, heap.contains(2));
        assertEquals(false, heap.contains(110));

        heap.remove(2);
        assertEquals(false, heap.contains(2));
    }

    @Test(timeout = SECOND)
    public void testContainsNull() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        heap.add(3);
        heap.add(6);

        try {
            heap.contains(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing
        }
    }

    @Test(timeout = SECOND)
    public void testReplace() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{3, 2, 6, 18, 110});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        heap.add(values[0]);
        heap.add(values[1]);
        heap.add(values[2]);
        heap.add(values[3]);

        heap.replace(values[1], values[4]);
        assertEquals(false, heap.contains(values[1]));
        assertEquals(true, heap.contains(values[4]));
        assertEquals(values[0], heap.peekMin());
    }

    @Test(timeout = SECOND)
    public void testReplaceWithWrong() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        heap.add(3);
        heap.add(6);
        heap.add(18);
        heap.add(2);

        try {
            heap.replace(9, 11);
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing
        }

        try {
            heap.replace(2, 18);
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing
        }
    }

    @Test(timeout = SECOND)
    public void testIsEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        assertEquals(true, heap.isEmpty());

        heap.add(3);

        assertEquals(false, heap.isEmpty());
    }


    /**
     * A comparable wrapper class for ints. Uses reference equality so that two different IntWrappers
     * with the same value are not necessarily equal--this means that you may have multiple different
     * IntWrappers with the same value in a heap.
     */
    public static class IntWrapper implements Comparable<IntWrapper> {
        private final int val;

        public IntWrapper(int value) {
            this.val = value;
        }

        public static IntWrapper[] createArray(int[] values) {
            IntWrapper[] output = new IntWrapper[values.length];
            for (int i = 0; i < values.length; i++) {
                output[i] = new IntWrapper(values[i]);
            }
            return output;
        }

        @Override
        public int compareTo(IntWrapper o) {
            return Integer.compare(val, o.val);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public int hashCode() {
            return this.val;
        }

        @Override
        public String toString() {
            return Integer.toString(this.val);
        }
    }

    /**
     * A helper method for accessing the private array inside a heap using reflection.
     */
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> Comparable<T>[] getArray(IPriorityQueue<T> heap) {
        return getField(heap, "heap", Comparable[].class);
    }

}
