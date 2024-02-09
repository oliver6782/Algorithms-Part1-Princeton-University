import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] p;
    private final int experiments;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Invalid size or trials");
        }
        p = new double[trials];
        experiments = trials;

        // implement simulation trials times
        for (int i = 0; i < experiments; i++) {
            Percolation obj = new Percolation(n);
            while (!obj.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                obj.open(row, col);
            }
            p[i] = (double) obj.numberOfOpenSites() / (n * n);
        }
    }


    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(p);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(p);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(experiments);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(experiments);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats run = new PercolationStats(Integer.parseInt(args[0]),
                                                    Integer.parseInt(args[1]));
        StdOut.println("mean =                    " + run.mean());
        StdOut.println("stddev =                  " + run.stddev());
        StdOut.println(
                "95% confidence interval = " + "[" + run.confidenceLo() + "," + run.confidenceHi()
                        + "]");
    }
}
