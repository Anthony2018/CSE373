package misc;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSorter extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testSortLessThanK() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(3);
        list.add(8);
        list.add(2);

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(3, top.size());

        assertEquals(2, top.get(0));
        assertEquals(3, top.get(1));
        assertEquals(8, top.get(2));
    }

    @Test(timeout=SECOND)
    public void testSortMoreThanK() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(3);
        list.add(8);
        list.add(2);
        list.add(11);
        list.add(23);
        list.add(5);

        IList<Integer> top = Sorter.topKSort(4, list);
        assertEquals(4, top.size());

        assertEquals(5, top.get(0));
        assertEquals(8, top.get(1));
        assertEquals(11, top.get(2));
        assertEquals(23, top.get(3));
    }


    @Test(timeout=SECOND)
    public void testZeroK() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(3);
        list.add(8);
        list.add(2);

        IList<Integer> top = Sorter.topKSort(0, list);
        assertEquals(0, top.size());
    }

    @Test(timeout=SECOND)
    public void testWithLargeData() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(995 + i, top.get(i));
        }

        IList<Integer> top2 = Sorter.topKSort(1000, list);
        assertEquals(1000, top2.size());
        for (int i = 0; i < top2.size(); i++) {
            assertEquals(i, top2.get(i));
        }
    }
}
