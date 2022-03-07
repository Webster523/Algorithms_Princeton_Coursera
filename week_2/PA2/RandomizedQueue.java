/* *****************************************************************************
 *  Name:              Weixiao Chen
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] elements;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        elements = (Item[]) new Object[1];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // helper method 1: to resize the instance array - elements
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = elements[i];
        }
        elements = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        // double the array when array is full
        if (size == elements.length) {
            resize(2 * size);
        }
        elements[size] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int rand = StdRandom.uniform(size);
        size--;
        Item element = elements[rand];
        elements[rand] = elements[size]; // insert the last element in array elements into the index of random - to avoid any holes in array elements
        elements[size] = null; // set the last element to be null, to avoid object loitering

        // shrink the array when array is one-quarter full
        if (size > 0 && size == elements.length / 4) {
            resize(elements.length / 2);
        }
        return element;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int rand = StdRandom.uniform(size);
        return elements[rand];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int index;
        private Item[] copy;

        public RandomizedQueueIterator() {
            index = 0;
            copy = (Item[]) new Object[elements.length];
            for (int i = 0; i < size; i++) {
                copy[i] = elements[i];
            }
            shuffle(copy);
        }

        // helper method 2: to shuffle the array, which can make sure that
        // each iterator maintains its own random order.
        private void exchange(Item[] arr, int i, int j) {
            Item swap = arr[i];
            arr[i] = arr[j];
            arr[j] = swap;
        }

        // helper method 3: Knuth shuffle
        private void shuffle(Item[] arr) {
            for (int i = 0; i < size; i++) {
                int r = StdRandom.uniform(i + 1);
                exchange(arr, i, r);
            }
        }

        public boolean hasNext() {
            return index < size;
        }

        public Item next() {
            if (index == size || copy[index] == null) {
                throw new NoSuchElementException();
            }
            Item element = copy[index];
            index++;
            return element;

        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rqueue = new RandomizedQueue<String>();
        // unit testing all corner cases
        // try {
        //     rqueue.enqueue(null);
        // }
        // catch (IllegalArgumentException e) {
        //     e.printStackTrace();
        // }
        //
        // try {
        //     System.out.println(rqueue.dequeue());
        // }
        // catch (NoSuchElementException e) {
        //     e.printStackTrace();
        // }
        //
        // try {
        //     System.out.println(rqueue.sample());
        // }
        // catch (NoSuchElementException e) {
        //     e.printStackTrace();
        // }

        Iterator<String> iter = rqueue.iterator();
        // try {
        //     System.out.println(iter.next());
        // }
        // catch (NoSuchElementException e) {
        //     e.printStackTrace();
        // }
        //
        // try {
        //     iter.remove();
        // }
        // catch (UnsupportedOperationException e) {
        //     e.printStackTrace();
        // }

        System.out.println(rqueue.isEmpty());
        System.out.println(rqueue.size());
        rqueue.enqueue("bat");
        rqueue.enqueue("ant");
        rqueue.enqueue("cat");
        rqueue.enqueue("fox");
        rqueue.enqueue("dog");
        rqueue.enqueue("cow");
        System.out.println(rqueue.isEmpty());
        System.out.println(rqueue.size());

        System.out.println(rqueue.sample());
        System.out.println(rqueue.size());

        System.out.println(rqueue.dequeue());
        System.out.println(rqueue.size());

        System.out.println();
        iter = rqueue.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }

        System.out.println();
        Iterator<String> iter1 = rqueue.iterator();
        while (iter1.hasNext()) {
            System.out.println(iter1.next());
        }

    }
}

