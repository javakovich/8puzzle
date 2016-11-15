import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

/**
 * Created by slazakovich on 9/26/2016.
 */
public class Solver {

    private boolean initialSolvable = false;
    private boolean twinSolvable = false;
    private SearchNode searchNode1;
    private SearchNode searchNode2;
    private Stack<Board> solutions;
    private int movesToSolution = 0;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        MinPQ<SearchNode> initialMinPQ = new MinPQ<SearchNode>(new Comparator<SearchNode>() {
            @Override
            public int compare(SearchNode o1, SearchNode o2) {
                return o1.getPriority() - o2.getPriority();
            }
        });

        MinPQ<SearchNode> twinMinPQ = new MinPQ<SearchNode>(new Comparator<SearchNode>() {
            @Override
            public int compare(SearchNode o1, SearchNode o2) {
                return o1.getPriority() - o2.getPriority();
            }
        });

        initialMinPQ.insert(new SearchNode(initial, 0, null));
        twinMinPQ.insert(new SearchNode(initial.twin(), 0, null));

        searchNode1 = initialMinPQ.delMin();
        searchNode2 = twinMinPQ.delMin();
        while (!(initialSolvable = searchNode1.getBoard().isGoal()) && !(twinSolvable = searchNode2.getBoard().isGoal())) {
            Iterable<Board> neighbors1 = searchNode1.getBoard().neighbors();
            for (Board neighbor : neighbors1) {
                if (searchNode1.getPrevious() == null || !neighbor.equals(searchNode1.getPrevious().getBoard())) {
                    initialMinPQ.insert(new SearchNode(neighbor, searchNode1.getMoves() + 1, searchNode1));
                }
            }
            Iterable<Board> neighbors2 = searchNode2.getBoard().neighbors();
            for (Board neighbor : neighbors2) {
                if (searchNode2.getPrevious() == null || !neighbor.equals(searchNode2.getPrevious().getBoard())) {
                    twinMinPQ.insert(new SearchNode(neighbor, searchNode2.getMoves() + 1, searchNode2));
                }
            }
            searchNode1 = initialMinPQ.delMin();
            searchNode2 = twinMinPQ.delMin();
        }
        movesToSolution = searchNode1.getMoves();
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return initialSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (initialSolvable) {
            return movesToSolution;
        } else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (initialSolvable) {
            if (solutions != null) return solutions;
            solutions = new Stack<Board>();
            while (searchNode1 != null) {
                solutions.push(searchNode1.getBoard());
                searchNode1 = searchNode1.getPrevious();
            }
            return solutions;
        } else {
            return null;
        }
    }

    private class SearchNode {

        private final Board board;
        private final int priority;
        private final SearchNode previous;
        private final int moves;

        SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.priority = moves + board.manhattan();
            this.previous = previous;
        }

        public Board getBoard() {
            return board;
        }

        public SearchNode getPrevious() {
            return previous;
        }

        public int getPriority() {
            return priority;
        }

        public int getMoves() {
            return moves;
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        Solver solver = new Solver(initial);
        if (!solver.isSolvable()) StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            int k = 0;
            for (Board board : solver.solution()) {
                StdOut.println("Number: " + ++k);
                StdOut.println(board);
            }
        }
    }
}
