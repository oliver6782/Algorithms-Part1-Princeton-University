import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Permutation <k>");
        }

        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();

        // Read strings from standard input and enqueue them
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            rq.enqueue(item);
        }

        // Print k items from the randomized queue
        Iterator<String> iterator = rq.iterator();
        for (int i = 0; i < k && iterator.hasNext(); i++) {
            StdOut.println(iterator.next());
        }
    }
}
