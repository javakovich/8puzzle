import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

/**
 * Created by slazakovich on 9/26/2016.
 */
public class Board {

    private final int size;
    private final int[] innerBoard;
    private final int n;
    private int hamming = -1;
    private int manhattan = -1;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) throw new NullPointerException();
        n = blocks.length;
        size = n * n;
        innerBoard = new int[size];
        int k = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                innerBoard[k++] = blocks[i][j];
            }
        }
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of blocks out of place
    public int hamming() {
        if (hamming != -1) return hamming;
        hamming = 0;
        for (int i = 0; i < innerBoard.length; i++) {
            if (i + 1 != innerBoard[i] && innerBoard[i] != 0) {
                hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (manhattan != -1) return manhattan;
        manhattan = 0;
        for (int i = 0; i < innerBoard.length; i++) {
            if (innerBoard[i] != 0) {
                int x0 = (innerBoard[i] - 1) % n;
                int y0 = (innerBoard[i] -1) / n;
                int x = i % n;
                int y = i / n;
                int difference = Math.abs(x - x0) + Math.abs(y - y0);
                manhattan += difference;
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < size - 1; i++) {
            if ((i + 1) != innerBoard[i]) {
                return false;
            }
        }
        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[] twinBoard = new int[size];
        for (int i = 0; i < size; i++) {
            twinBoard[i] = innerBoard[i];
        }

        for (int i = 0; i < size - 1; i++) {
            if (twinBoard[i] == 0) continue;
            boolean swapped = false;
            for (int j = i + 1; j < size; j++) {
                if (twinBoard[j] == 0) continue;
                int temp = twinBoard[i];
                twinBoard[i] = twinBoard[j];
                twinBoard[j] = temp;
                swapped = true;
                break;
            }
            if (swapped) break;
        }

        int k = 0;
        int[][] b = new int[n][];
        for (int i = 0; i < n; i++) {
            int[] a = new int[n];
            for (int j = 0; j < n; j++) {
                a[j] = twinBoard[k++];
            }
            b[i] = a;
        }

        return new Board(b);

    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.innerBoard.length != that.innerBoard.length) return false;
        for (int i = 0; i < size; i++) {
            if (this.innerBoard[i] != that.innerBoard[i]) {
                return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>(4);
        int empty = 0;
        for (int i = 0; i < size; i++) {
            if (innerBoard[i] == 0) {
                empty = i;
                break;
            }
        }

        int x = empty / n;
        int y = (empty + 1) - x * n - 1;

        if (x < n && (y + 1) < n) {
            int[] innerBoard1 = new int[size];
            for (int i = 0; i < size; i++) {
                innerBoard1[i] = innerBoard[i];
            }

            int neighbor = x * n + y + 1;
            int temp = innerBoard1[empty];
            innerBoard1[empty] = innerBoard1[neighbor];
            innerBoard1[neighbor] = temp;

            int k = 0;
            int[][] b = new int[n][];
            for (int i = 0; i < n; i++) {
                int[] a = new int[n];
                for (int j = 0; j < n; j++) {
                    a[j] = innerBoard1[k++];
                }
                b[i] = a;
            }
            neighbors.add(new Board(b));
        }
        if ((x + 1) < n && y < n) {
            int[] innerBoard2 = new int[size];
            for (int i = 0; i < size; i++) {
                innerBoard2[i] = innerBoard[i];
            }

            int neighbor = (x + 1) * n + y;
            int temp = innerBoard2[empty];
            innerBoard2[empty] = innerBoard2[neighbor];
            innerBoard2[neighbor] = temp;

            int k = 0;
            int[][] b = new int[n][];
            for (int i = 0; i < n; i++) {
                int[] a = new int[n];
                for (int j = 0; j < n; j++) {
                    a[j] = innerBoard2[k++];
                }
                b[i] = a;
            }
            neighbors.add(new Board(b));
        }

        if ((x - 1) >= 0 && y < n) {
            int[] innerBoard3 = new int[size];
            for (int i = 0; i < size; i++) {
                innerBoard3[i] = innerBoard[i];
            }

            int neighbor = (x - 1) * n + y;
            int temp = innerBoard3[empty];
            innerBoard3[empty] = innerBoard3[neighbor];
            innerBoard3[neighbor] = temp;

            int k = 0;
            int[][] b = new int[n][];
            for (int i = 0; i < n; i++) {
                int[] a = new int[n];
                for (int j = 0; j < n; j++) {
                    a[j] = innerBoard3[k++];
                }
                b[i] = a;
            }
            neighbors.add(new Board(b));
        }

        if (x < n && (y - 1) >= 0) {
            int[] innerBoard3 = new int[size];
            for (int i = 0; i < size; i++) {
                innerBoard3[i] = innerBoard[i];
            }

            int neighbor = x * n + y - 1;
            int temp = innerBoard3[empty];
            innerBoard3[empty] = innerBoard3[neighbor];
            innerBoard3[neighbor] = temp;

            int k = 0;
            int[][] b = new int[n][];
            for (int i = 0; i < n; i++) {
                int[] a = new int[n];
                for (int j = 0; j < n; j++) {
                    a[j] = innerBoard3[k++];
                }
                b[i] = a;
            }
            neighbors.add(new Board(b));
        }


        return neighbors;

    }


    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < size; i++) {
            s.append(String.format("%2d ", this.innerBoard[i]));
            if ((i + 1) % n == 0) s.append("\n");
        }
        return s.toString();
    }


    // unit tests (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        String toString = initial.toString();
        int hamming = initial.hamming();
        int manhattan = initial.manhattan();
        Board twin = initial.twin();
        for (Board neighbor : initial.neighbors()) {
            StdOut.println(neighbor);
        }

    }
}
