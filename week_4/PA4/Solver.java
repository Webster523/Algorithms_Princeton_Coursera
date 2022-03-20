/* *****************************************************************************
 *  Name:              Weixiao Chen
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Board initial;
    private final boolean isSolvable;
    private final int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        Board twin = initial.twin();
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(initial, 0, null));
        pq.insert(new SearchNode(twin, 0, null));

        SearchNode node = null;
        while (!pq.isEmpty()) {
            node = pq.delMin();
            if (node.board.isGoal())
                break;

            Iterable<Board> iter = node.board.neighbors();
            for (Board neighbour : iter) {
                if (node.prev == null || !neighbour.equals(node.prev.board)) {
                    pq.insert(new SearchNode(neighbour, node.moves + 1, node));
                }
            }
        }

        SearchNode temp = node;
        while (temp.prev != null) {
            temp = temp.prev;
        }

        if (temp.board.equals(initial)) {
            this.initial = initial;
            isSolvable = true;
            moves = node.moves;
        }
        else {
            this.initial = null;
            isSolvable = false;
            moves = -1;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable)
            return null;

        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();

        SearchNode node = new SearchNode(initial, 0, null);
        pq.insert(node);
        while (!pq.isEmpty()) {
            node = pq.delMin();

            if (node.board.isGoal())
                break;
            else {
                Iterable<Board> iter = node.board.neighbors();
                for (Board neighbour : iter) {
                    if (node.prev == null || !neighbour.equals(node.prev.board)) {
                        pq.insert(new SearchNode(neighbour, node.moves + 1, node));
                    }
                }
            }
        }
        Stack<Board> stack = new Stack<Board>();
        while (node != null) {
            stack.push(node.board);
            node = node.prev;
        }

        Queue<Board> queue = new Queue<Board>();
        while (!stack.isEmpty()) {
            Board temp = stack.pop();
            queue.enqueue(temp);
        }
        return queue;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode prev;
        private final int priority;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.priority = board.manhattan() + moves;
        }

        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        // In in = new In(args[0]);
        // int n = in.readInt();
        // int[][] tiles = new int[n][n];
        // for (int i = 0; i < n; i++)
        //     for (int j = 0; j < n; j++)
        //         tiles[i][j] = in.readInt();
        int[][] tiles = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
