import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private Board initial;
    private Stack<Board> solutionSteps;
    private boolean solvable = false;


    private class SearchNode {
        private Board puzzleState;
        private int moves;
        private int priority;
        private SearchNode prev;

        public SearchNode(Board initial, int moves, SearchNode prev) {
            this.puzzleState = initial;
            this.moves = moves;
            this.priority = moves + initial.manhattan();
            this.prev = prev;
        }

    }

    private static class SearchNodeComparator implements Comparator<SearchNode> {
        public int compare(SearchNode node1, SearchNode node2) {
            int priority1 = node1.priority;
            int priority2 = node2.priority;
            return Integer.compare(priority1, priority2);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("null argument");
        this.initial = initial;

        solutionSteps = new Stack<>();
        puzzleSolve();

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return solutionSteps.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return solutionSteps;
    }

    private void puzzleSolve() {
        Board twinBoard = initial.twin();
        MinPQ<SearchNode> puzzleTree1 = new MinPQ<>(new SearchNodeComparator());
        MinPQ<SearchNode> puzzleTree2 = new MinPQ<>(new SearchNodeComparator());
        puzzleTree1.insert(new SearchNode(initial, 0, null));
        puzzleTree2.insert(new SearchNode(twinBoard, 0, null));


        while (!puzzleTree1.isEmpty() && !puzzleTree2.isEmpty()) {
            SearchNode curNode1 = puzzleTree1.delMin();
            SearchNode curNode2 = puzzleTree2.delMin();
            if (curNode1.puzzleState.isGoal()) {
                while (curNode1 != null) {
                    solutionSteps.push(curNode1.puzzleState);
                    curNode1 = curNode1.prev;
                }
                solvable = true;
                return;

            }
            if (curNode2.puzzleState.isGoal()) {
                return;
            }

            for (Board neighbor : curNode1.puzzleState.neighbors()) {
                if (curNode1.prev == null || !neighbor.equals(curNode1.prev.puzzleState)) {
                    puzzleTree1.insert(new SearchNode(neighbor, curNode1.moves + 1, curNode1));
                }
            }
            for (Board neighbor : curNode2.puzzleState.neighbors()) {
                if (curNode2.prev == null || !neighbor.equals(curNode2.prev.puzzleState)) {
                    puzzleTree2.insert(new SearchNode(neighbor, curNode2.moves + 1, curNode2));
                }
            }

        }


    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

}
