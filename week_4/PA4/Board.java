/* *****************************************************************************
 *  Name:              Weixiao Chen
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;

public class Board {
    private final int[][] board;
    private final int dimension;
    private int zeroRow;
    private int zeroCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles.length;
        board = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = tiles[i][j];
                if (board[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        // While there are many concatenation, use string builder to create a string
        // since the operation "+" requires linear time.
        String format = "%" + dimension + "d";
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format(format, board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        if (isGoal())
            return 0;
        int count = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == zeroRow && j == zeroCol) {
                    continue;
                }
                else {
                    int[] goalCoordinate = getGoalCoordinate(board[i][j]);
                    if (i != goalCoordinate[0] || j != goalCoordinate[1])
                        count++;
                }
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (isGoal())
            return 0;
        int count = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == zeroRow && j == zeroCol) {
                    continue;
                }
                else {
                    int[] goalCoordinate = getGoalCoordinate(board[i][j]);
                    int diff = Math.abs(goalCoordinate[0] - i) + Math.abs(goalCoordinate[1] - j);
                    count += diff;
                }
            }
        }
        return count;
    }

    private int[] getGoalCoordinate(int tile) {
        // Since the number on board counts from 1 instead of 0,
        // we essentially find out the position of (tile - 1).
        tile--;
        int[] coordinate = new int[2];
        coordinate[0] = tile / dimension; // goal row
        coordinate[1] = tile % dimension; // goal column
        return coordinate;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int num = 1;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == dimension - 1 && j == dimension - 1) {
                    num = 0;
                }
                if (board[i][j] != num) {
                    return false;
                }
                num++;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // imitate slide page 10 of slide elementary symbol table
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;
        if (this.dimension != that.dimension)
            return false;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (this.board[i][j] != that.board[i][j])
                    return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> queue = new Queue<Board>();

        Board b = getNeighbour(zeroRow, zeroCol - 1); // left
        if (b != null)
            queue.enqueue(b);

        b = getNeighbour(zeroRow, zeroCol + 1); // right
        if (b != null)
            queue.enqueue(b);

        b = getNeighbour(zeroRow - 1, zeroCol); // top
        if (b != null)
            queue.enqueue(b);

        b = getNeighbour(zeroRow + 1, zeroCol); // bottom
        if (b != null)
            queue.enqueue(b);

        return queue;
    }

    private Board getNeighbour(int newRow, int newCol) {
        if (!isValid(newRow) || !isValid(newCol))
            return null;
        int[][] copy = copy();
        swap(copy, zeroRow, zeroCol, newRow, newCol);
        return new Board(copy);
    }

    private boolean isValid(int index) {
        if (0 <= index && index < dimension)
            return true;
        else
            return false;
    }

    private int[][] copy() {
        int[][] copy = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    private void swap(int[][] copy, int oldRow, int oldCol, int newRow, int newCol) {
        int temp = copy[newRow][newCol];
        copy[newRow][newCol] = copy[oldRow][oldCol];
        copy[oldRow][oldCol] = temp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] copy = copy();

        // I misunderstand the spec here. It does not require to generate a random twin every call.
        // It just require two specific or fixed tiles to be exchanged.

        // int row1 = StdRandom.uniform(dimension), col1 = StdRandom.uniform(dimension);
        // while (row1 == zeroRow && col1 == zeroCol) {
        //     row1 = StdRandom.uniform(dimension);
        //     col1 = StdRandom.uniform(dimension);
        // }
        //
        // int row2 = StdRandom.uniform(dimension), col2 = StdRandom.uniform(dimension);
        // while ((row2 == zeroRow && col2 == zeroCol) || (row2 == row1 && col2 == col1)){
        //     row2 = StdRandom.uniform(dimension);
        //     col2 = StdRandom.uniform(dimension);
        // }

        // The constructor receives an n-by-n array containing n*n integers. We can assume 2 <= n <= 128.
        // Using a mod operation to make sure we take elements not in the same row as the blank tile.
        // Then under our assumption, column number 0 and 1 must be valid.
        int row = (zeroRow + 1) % dimension;
        swap(copy, row, 0, row, 1);
        return new Board(copy);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] arr = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        // int[][] arr = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
        // int[][] arr = {{2, 1}, {3, 0}};
        // int[][] arr = {{6, 3, 1}, {4, 8, 2}, {5, 7, 0}};
        // Board board = new Board(arr);

        // System.out.println(board);
        // System.out.println();
        //
        // System.out.println(board.dimension());
        // System.out.println();
        //
        // System.out.println(board.hamming());
        // System.out.println();
        //
        // System.out.println(board.manhattan());
        // System.out.println();
        //
        // System.out.println(board.isGoal());
        // System.out.println();
        //
        // System.out.println(board.twin());
        // System.out.println();

        // Iterable<Board> s = board.neighbors();
        // for (Board b : s) {
        //     System.out.println(b);
        // }
        // System.out.println();

        // Board board1 = new Board(arr);
        // System.out.println(board.equals(board1));
        // System.out.println();


        // int[][] arr1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        // Board board2 = new Board(arr1);
        //
        // System.out.println(board2.hamming());
        // System.out.println();
        //
        // System.out.println(board2.manhattan());
        // System.out.println();
        //
        // System.out.println(board2.isGoal());
        // System.out.println();
        //
        // System.out.println(board.equals(board2));
        // System.out.println();
    }
}
