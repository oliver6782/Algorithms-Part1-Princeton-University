import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int TOP_SITE = 0;
    private final int size;
    private int openSites;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufBackwash;
    private final boolean[] opened;
    private final int bottomSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Invalid size");
        }
        this.size = n;
        bottomSite = size * size + 1;
        openSites = 0;
        uf = new WeightedQuickUnionUF(size * size + 1);
        ufBackwash = new WeightedQuickUnionUF(size * size + 2);
        opened = new boolean[size * size + 2];
        opened[TOP_SITE] = true;
        opened[bottomSite] = true;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkIndexRange(row, col);
        if (isOpen(row, col)) {
            return;
        }
        opened[elementNumber(row, col)] = true;
        openSites++;

        // connect newly opened site with neighbouring open sites
        // union 1st row to virtue top
        if (row == 1) {
            uf.union(TOP_SITE, elementNumber(row, col));
            ufBackwash.union(TOP_SITE, elementNumber(row, col));
        }
        // union bottom row to virtue bottom
        if (row == size) {
            ufBackwash.union(bottomSite, elementNumber(row, col));
        }
        // check neighbouring open sites and union with them
        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(elementNumber(row, col), elementNumber(row - 1, col));
            ufBackwash.union(elementNumber(row, col), elementNumber(row - 1, col));
        }
        if (row < size && isOpen(row + 1, col)) {
            uf.union(elementNumber(row, col), elementNumber(row + 1, col));
            ufBackwash.union(elementNumber(row, col), elementNumber(row + 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(elementNumber(row, col), elementNumber(row, col - 1));
            ufBackwash.union(elementNumber(row, col), elementNumber(row, col - 1));
        }
        if (col < size && isOpen(row, col + 1)) {
            uf.union(elementNumber(row, col), elementNumber(row, col + 1));
            ufBackwash.union(elementNumber(row, col), elementNumber(row, col + 1));
        }
    }

    private void checkIndexRange(int row, int col) {
        if (row < 1 || row > this.size || col < 1 || col > this.size) {
            throw new IllegalArgumentException("Out of range");
        }
    }

    // find the number of elements in the matrix
    private int elementNumber(int row, int col) {
        checkIndexRange(row, col);
        return (row - 1) * this.size + col;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkIndexRange(row, col);
        return opened[elementNumber(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkIndexRange(row, col);
        return (uf.find(TOP_SITE) == uf.find(elementNumber(row, col)) && isOpen(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return (ufBackwash.find(TOP_SITE) == ufBackwash.find(bottomSite));
    }

    public static void main(String[] args) {
    }

}
