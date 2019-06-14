package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;


/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified. You should give your tests
 * with a timeout of 1 second.
 *
 * This test extends the BaseTestDoubleLinkedList class. This means that
 * you can use the helper methods defined within BaseTestDoubleLinkedList.
 * @see BaseTestDoubleLinkedList
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDoubleLinkedListDelete extends BaseTestDoubleLinkedList {
    @Test(timeout=SECOND)
    public void testDeleteMiddleElement() {
        // Feel free to modify or delete this dummy test.
        IList<String> list = makeBasicList();
        list.delete(1);
        assertListValidAndMatches(new String[] {"a", "c"}, list);

        IList<Integer> listInt = new DoubleLinkedList<>();
        listInt.add(1);
        listInt.add(2);
        listInt.add(3);
        listInt.add(4);
        listInt.add(5);

        listInt.delete(3);
        assertListValidAndMatches(new Integer[] {1, 2, 3, 5}, listInt);
    }

    @Test(timeout=SECOND)
    public void testDeleteIndexOfAndDeleteMiddle() {
        IList<String> list = makeBasicList();
        list.delete(1);
        assertFalse(list.contains("b"));

        IList<Integer> listInt = new DoubleLinkedList<>();
        listInt.add(1);
        listInt.add(2);
        listInt.add(3);
        listInt.add(4);
        listInt.add(5);

        listInt.delete(3);
        assertListValidAndMatches(new Integer[] {1, 2, 3, 5}, listInt);
        assertFalse(listInt.contains(4));
    }

    @Test(timeout=SECOND)
    public void testDeleteUpdateSize() {
        IList<String> list = makeBasicList();
        list.delete(1);
        assertEquals(2, list.size());

        IList<Integer> listInt = new DoubleLinkedList<>();
        listInt.add(1);
        listInt.add(2);
        listInt.add(3);
        listInt.add(4);
        listInt.add(5);

        listInt.delete(3);
        assertEquals(4, listInt.size());
    }

    @Test(timeout=SECOND)
    public void testDeleteFrontElements() {
        IList<String> list = makeBasicList();
        list.delete(0);
        assertListValidAndMatches(new String[] {"b", "c"}, list);

        IList<Integer> listInt = new DoubleLinkedList<>();
        listInt.add(1);
        listInt.add(2);
        listInt.add(3);
        listInt.add(4);
        listInt.add(5);

        listInt.delete(0);
        assertListValidAndMatches(new Integer[] {2, 3, 4, 5}, listInt);
    }

    @Test(timeout=SECOND)
    public void testDeleteBackElements() {
        IList<String> list = makeBasicList();
        list.delete(2);
        assertListValidAndMatches(new String[] {"a", "b"}, list);

        IList<Integer> listInt = new DoubleLinkedList<>();
        listInt.add(1);
        listInt.add(2);
        listInt.add(3);
        listInt.add(4);
        listInt.add(5);

        listInt.delete(4);
        assertListValidAndMatches(new Integer[] {1, 2, 3, 4}, listInt);
    }

    @Test(timeout=SECOND)
    public void testDeleteDuplicates() {
        IList<String> list = makeBasicList();
        list.add("a");
        list.delete(0);
        list.delete(2);
        assertListValidAndMatches(new String[] {"b", "c"}, list);
        assertFalse(list.contains("a"));

        IList<Integer> listInt = new DoubleLinkedList<>();
        listInt.add(1);
        listInt.add(2);
        listInt.add(3);
        listInt.add(3);
        listInt.add(4);
        listInt.add(5);
        listInt.add(3);

        listInt.delete(2);
        listInt.delete(2);
        listInt.delete(4);
        assertListValidAndMatches(new Integer[] {1, 2, 4, 5}, listInt);
        assertFalse(listInt.contains(3));
    }

    @Test(timeout=SECOND)
    public void testDeleteSingleElementList() {
        IList<Integer> listInt = new DoubleLinkedList<>();
        listInt.add(1);
        listInt.delete(0);
        assertTrue(listInt.size()==0);
    }

    @Test(timeout=SECOND)
    public void testDeleteNullElement() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(null);
        list.delete(3);
        assertFalse(list.contains(null));
    }

    @Test(timeout=SECOND)
    public void testDeleteOutOfBoundsThrowsException() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        try {
            list.delete(10);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing
        }

        try {
            list.delete(-10);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing
        }
    }


    // Above are some examples of provided assert methods from JUnit,
    // but in these tests you will also want to use a custom assert
    // we have provided you in BaseTestDoubleLinkedList called
    // assertListValidAndMatches. It will check many properties of
    // your DoubleLinkedList so you will want to use it frequently.
    // For usage examples, you can refer to TestDoubleLinkedList,
    // and refer to BaseTestDoubleLinkedList for the method comment.
}
