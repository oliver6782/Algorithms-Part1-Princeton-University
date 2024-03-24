import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {
    private int[][] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        int n = tiles.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        int n = board.length;
        StringBuilder sb = new StringBuilder();

        // append the grid size in the first line
        sb.append(n);
        sb.append("\n");

        // for each row and each col, append a space and the corresponding number.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(" ");
                sb.append(board[i][j]);
            }
            sb.append("\n"); // at the end of each line, append a new line.
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return board.length;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingCount = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != 0 && board[i][j] != board.length * i + j + 1) {
                    hammingCount++;
                }
            }
        }
        return hammingCount;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int n = board.length;
        int manhattanCount = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0) {
                    int value = board[i][j] - 1; // Correct value for the tile
                    int goalRow = value / n; // Goal row index
                    int goalCol = value % n; // Goal column index
                    int iDistance = Math.abs(i - goalRow); // Manhattan distance for rows
                    int jDistance = Math.abs(j - goalCol); // Manhattan distance for columns
                    manhattanCount += iDistance + jDistance; // Add both distances
                }
            }
        }
        return manhattanCount;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int n = board.length;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != 0 && board[i][j] != n * i + j + 1) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {

        // getClass function ensures that y is the same class as object it is compared to
        if (y == null || this.getClass() != y.getClass()) return false;
        Board other = (Board) y;
        if (this.dimension() != other.dimension()) {
            return false;
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (this.board[i][j] != other.board[i][j]) return false;
            }
        }
        return true;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        int n = board.length;
        int row = -1;
        int col = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        if (row > 0) {
            int[][] temp = copyBoard(board);
            // cannot use @ArrayCopy or clone, because they copy the reference to the arrays
            // within the board, not the arrays themselves.
            // Therefore, modifying one of the copied arrays will reflect changes in the original board as well.
            temp[row][col] = temp[row - 1][col];
            temp[row - 1][col] = 0;
            neighbors.add(new Board(temp));
        }
        if (row < n - 1) {
            int[][] temp = copyBoard(board);
            temp[row][col] = temp[row + 1][col];
            temp[row + 1][col] = 0;
            neighbors.add(new Board(temp));
        }
        if (col > 0) {
            int[][] temp = copyBoard(board);
            temp[row][col] = temp[row][col - 1];
            temp[row][col - 1] = 0;
            neighbors.add(new Board(temp));
        }
        if (col < n - 1) {
            int[][] temp = copyBoard(board);
            temp[row][col] = temp[row][col + 1];
            temp[row][col + 1] = 0;
            neighbors.add(new Board(temp));

        }
        return neighbors;
    }

    // Deep copy of the board array
    private int[][] copyBoard(int[][] original) {
        int n = original.length;
        int[][] copy = new int[n][];
        for (int i = 0; i < n; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int n = dimension();
        int[][] twinTiles = copyBoard(board);
        int x1 = -1, y1 = -1, x2 = -1, y2 = -1;

        // Find the first non-empty tile
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (twinTiles[i][j] != 0) {
                    x1 = i;
                    y1 = j;
                    break;
                }
            }
        }
        // Find the second non-empty tile (different from the first one)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (twinTiles[i][j] != 0 && (i != x1 || j != y1)) {
                    x2 = i;
                    y2 = j;
                    break;
                }
            }
        }
        // Swap the positions of the two tiles
        int temp = twinTiles[x1][y1];
        twinTiles[x1][y1] = twinTiles[x2][y2];
        twinTiles[x2][y2] = temp;
        return new Board(twinTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] input = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        Board demo = new Board(input);
        StdOut.println(demo.toString());
        StdOut.println("Manhattan distance: " + demo.manhattan());
        for (Board neighbor : demo.neighbors()) {
            StdOut.println(neighbor.toString());
        }

    }

}
