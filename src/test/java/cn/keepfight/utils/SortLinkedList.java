package cn.keepfight.utils;


class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class SortLinkedList {

    // merge sort
    public static ListNode mergeSortList(ListNode head) {

        if (head == null || head.next == null)
            return head;

        int count = 0;
        ListNode p = head;
        while (p != null) {
            count++;
            p = p.next;
        }

        int middle = count / 2;

        ListNode l = head, r = null;
        ListNode p2 = head;
        int countHalf = 0;
        while (p2 != null) {
            countHalf++;
            ListNode next = p2.next;

            if (countHalf == middle) {
                p2.next = null;
                r = next;
            }
            p2 = next;
        }

        ListNode h1 = mergeSortList(l);
        ListNode h2 = mergeSortList(r);

        ListNode merged = merge(h1, h2);

        return merged;
    }

    public static ListNode merge(ListNode l, ListNode r) {
        ListNode p1 = l;
        ListNode p2 = r;

        ListNode fakeHead = new ListNode(100);
        ListNode pNew = fakeHead;

        while (p1 != null || p2 != null) {

            if (p1 == null) {
                pNew.next = new ListNode(p2.val);
                p2 = p2.next;
                pNew = pNew.next;
            } else if (p2 == null) {
                pNew.next = new ListNode(p1.val);
                p1 = p1.next;
                pNew = pNew.next;
            } else {
                if (p1.val < p2.val) {
                    pNew.next = new ListNode(p1.val);
                    p1 = p1.next;
                    pNew = pNew.next;
                } else if (p1.val == p2.val) {
                    pNew.next = new ListNode(p1.val);
                    pNew.next.next = new ListNode(p1.val);
                    pNew = pNew.next.next;
                    p1 = p1.next;
                    p2 = p2.next;

                } else {
                    pNew.next = new ListNode(p2.val);
                    p2 = p2.next;
                    pNew = pNew.next;
                }
            }
        }

        return fakeHead.next;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            test((i + 1) * 10);
        }
    }

    static void test(int num) {
        ListNode as = randomArr(num);
        long x = System.nanoTime();
        mergeSortList(as);
        long y = System.nanoTime();
        System.out.println("run " + num + " with time spend:" + (y - x));
    }

    public static ListNode randomArr(int length) {
        ListNode as=null;
        for (int i = 0; i < length; i++) {
            ListNode n2 = new ListNode((int)(Math.random() * length));
            n2.next=as;
            as = n2;
        }
        return as;
    }
}
