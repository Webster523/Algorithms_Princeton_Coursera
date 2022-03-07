/* *****************************************************************************
 *  Name:              Weixiao Chen
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item element;
        private Node pred;
        private Node next;

        public Node(Item element) {
            this.element = element;
            pred = null;
            next = null;
        }
    }

    private Node first;
    private Node last;
    private int size;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        Node ins = new Node(item);
        if (isEmpty()) {
            first = ins;
            last = ins;
        }
        else {
            ins.next = first;
            first.pred = ins;
            first = ins;
        }
        size++; // All changes to size must be done after the insertion or deletion finishes, since size is used to signal the previous state of "whether the deque is empty or not".
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node ins = new Node(item);
        if (isEmpty()) {
            first = ins;
            last = ins;
        }
        else {
            ins.pred = last;
            last.next = ins;
            last = ins;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        Node del = first;
        if (size == 1) {
            first = null;
            last = null;
        }
        else {
           first = del.next;
           del.next.pred = null;
        }
        size--;
        return del.element;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        Node del = last;
        if (size == 1) {
            first = null;
            last = null;
        }
        else {
            last = del.pred;
            del.pred.next = null;
        }
        size--;
        return del.element;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            Item element = current.element;
            current = current.next;
            return element;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();

        // testing for all corner cases
        // try {
        //     deque.addFirst(null);
        // }
        // catch (IllegalArgumentException e) {
        //     e.printStackTrace();
        // }
        //
        // try {
        //     deque.addLast(null);
        // }
        // catch (IllegalArgumentException e) {
        //     e.printStackTrace();
        // }
        //
        // try {
        //     deque.removeFirst();
        // }
        // catch (NoSuchElementException e) {
        //     e.printStackTrace();
        // }
        //
        // try {
        //     deque.removeLast();
        // }
        // catch (NoSuchElementException e) {
        //     e.printStackTrace();
        // }
        //
        Iterator<String> iter = deque.iterator();
        // try {
        //     iter.remove();
        // }
        // catch (UnsupportedOperationException e) {
        //     e.printStackTrace();
        // }
        //
        // try {
        //     iter.next();
        // }
        // catch (NoSuchElementException e) {
        //     e.printStackTrace();
        // }

        System.out.println(deque.isEmpty());
        deque.addFirst("ant");
        deque.addFirst("bat");
        deque.addLast("cat");
        deque.addLast("fox");
        System.out.println(deque.isEmpty());
        // for (String s : deque) {
        //     System.out.println(s);
        // }
        iter = deque.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        System.out.println(deque.size());

        System.out.println();
        deque.removeLast();
        iter = deque.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        System.out.println(deque.size());

        System.out.println();
        deque.removeFirst();
        iter = deque.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        System.out.println(deque.size());
    }
}
