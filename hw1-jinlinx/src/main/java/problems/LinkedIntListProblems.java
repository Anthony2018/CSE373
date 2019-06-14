package problems;

import datastructures.LinkedIntList;


// IntelliJ will complain that this is an unused import, but you should use ListNode variables
// in your solution, and then this error should go away.

import datastructures.LinkedIntList.ListNode;

/**
 * Parts b.iii, b.iv, and b.v should go here.
 *
 * (Implement reverse3, firstLast, and shift as described by the spec
 *  See the spec on the website for picture examples and more explanation!)
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not construct new ListNode objects (though you may have as many ListNode variables as you like).
 * - do not call any LinkedIntList methods
 * - do not construct any external data structures like arrays, queues, lists, etc.
 * - do not mutate the .data field of any nodes, change the list only by modifying links between nodes.
 * - Your solution should run in linear time with respect to the number of elements of the linked list.
 */

public class LinkedIntListProblems {

    // Reverses the 3 elements in the LinkedIntList (assume there are only 3 elements).
    public static void reverse3(LinkedIntList list) {
        ListNode first = list.front.next.next;
        ListNode second = list.front.next;
        ListNode third = list.front;
        list.front = first;
        list.front.next = second;
        list.front.next.next = third;
        list.front.next.next.next = null;
    }


    public static void shift(LinkedIntList list) {
        if (list.front == null)
        {
            return;
        }

        ListNode evenid = null;
        ListNode lastEven = null;
        ListNode oddid = null;
        ListNode lastodd = null;
        ListNode curr = list.front;
        int i = 0;

        while (curr != null) {
            if (i % 2 == 0) {
                if (evenid == null) {
                    evenid = curr;
                    lastEven = curr;
                } else {
                    lastEven.next = curr;
                    lastEven = curr;
                }
                curr = curr.next;
                lastEven.next = null;
            } else {
                if (oddid == null) {
                    oddid = curr;
                    lastodd = curr;
                } else {
                    lastodd.next = curr;
                    lastodd = curr;
                }
                curr = curr.next;
                lastodd.next = null;
            }

            i++;
        }

        list.front = evenid;
        lastEven.next = oddid;
    }




    public static void firstLast(LinkedIntList list) {
        if (list.front == null) {
            list.front = null;
        } else if (list.front.next == null) {
            list.front.next = null;
        } else {
            ListNode save = list.front;
            ListNode curr = list.front.next;
            list.front= curr;
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next= save;
            save.next = null;

        }


    }
}

