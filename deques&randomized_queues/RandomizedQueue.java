import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n = 0;
    private Item[] list;

    // construct an empty randomized queue
    public RandomizedQueue() {
        list = (Item[]) new Object[10];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == list.length) {
            resize(list.length * 2);
        }
        list[n++] = item;

    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniformInt(n);
        Item itemRemove = list[index];
        if (n > 0 && n == list.length / 4) {
            resize(list.length / 2);
        }
        list[index] = list[n - 1];
        list[n - 1]
                = null; // swap removed item with the last one of the array, avoid looping over the array
        n--;

        return itemRemove;
    }

    // resize array
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = list[i];
        }
        list = copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniformInt(n);
        return list[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedArrayIterator();
    }

    /**
     * private void shuffleArray() {
     * int len = list.length;
     * for (int i = 0; i < len; i++) {
     * int r = StdRandom.uniformInt(0, i + 1);
     * Item temp = list[i];
     * list[i] = list[r];
     * list[r] = temp;
     * }
     * }
     **/
    private class RandomizedArrayIterator implements Iterator<Item> {
        private int i = 0;
        private int[] randomIndex;

        public RandomizedArrayIterator() {
            randomIndex = new int[n];
            for (int j = 0; j < n; j++) {
                randomIndex[j] = j;
            }
            StdRandom.shuffle(randomIndex);
        }

        public boolean hasNext() {
            return i < n;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return list[randomIndex[i++]];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> test = new RandomizedQueue<>();
        test.enqueue(1);
        test.enqueue(2);
        test.enqueue(3);
        test.enqueue(4);
        for (int i : test) {
            StdOut.println(i);
        }

        StdOut.println("removed:" + test.dequeue());
        StdOut.println("ramdom sample: " + test.sample());

    }

}
