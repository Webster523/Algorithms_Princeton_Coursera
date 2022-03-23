/* *****************************************************************************
 *  Name:              Weixiao Chen
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF uf;  // maintain the connectivity
    private final WeightedQuickUnionUF ufWithoutBottom; // overcome the backwash
    private final int size;     // the dimension of the grid
    private int cntOpen;        // the number of open sites
    private boolean[] open;     // maintain if a site is open or blocked

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // for corner case - n <= 0
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        size = n;
        cntOpen = 0;
        open = new boolean[n * n + 2];
        for (int i = 0; i < open.length; i++) {
            if (i == 0 || i == open.length - 1) {
                // 0 - the top virtual site
                // (open.length - 1) - the bottom virtual site
                // set these two virtual sites to be true from the beginning
                open[i] = true;
            }
            else {
                open[i] = false;
            }
        }
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufWithoutBottom = new WeightedQuickUnionUF(n * n + 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // for corner case - row or col not in range [1, n]
        if (!isvalid(row) || !isvalid(col)) {
            throw new IllegalArgumentException();
        }

        int index = indexMapping(row, col);
        if (!open[index]) {
            // open the site itself
            open[index] = true;
            cntOpen++;

            // connect 'this' site to all of its adjacent open sites
            int left, right, up, down;
            if (col != 1) {
                // left = (row - 1) * size + (col - 1);
                left = indexMapping(row, col - 1);  // stick to the modular programming!
                if (open[left]) {
                    uf.union(index, left);
                    ufWithoutBottom.union(index, left);
                }
            }

            if (col != size) {
                // right = (row - 1) * size + (col + 1);
                right = indexMapping(row, col + 1);
                if (open[right]) {
                    uf.union(index, right);
                    ufWithoutBottom.union(index, right);
                }
            }

            if (row == 1) {
                up = 0;
            }
            else {
                // up = (row - 2) * size + col;
                up = indexMapping(row - 1, col);
            }
            if (open[up]) {
                uf.union(index, up);
                ufWithoutBottom.union(index, up);
            }


            if (row == size) {
                down = open.length - 1;
            }
            else {
                // down = row * size + col;
                down = indexMapping(row + 1, col);
            }
            if (open[down]) {
                uf.union(index, down);
                if (down != open.length - 1)
                    ufWithoutBottom.union(index, down);
            }
        }
    }

    // helper method 1: check whether the arg coordinate is valid or not
    private boolean isvalid(int num) {
        if (1 <= num && num <= size)
            return true;
        else
            return false;
    }

    // helper method 2: map 2D coordinate into 1D index
    private int indexMapping(int row, int col) {
        return (row - 1) * size + col;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        // for corner case - row or col not in range [1, n]
        if (!isvalid(row) || !isvalid(col)) {
            throw new IllegalArgumentException();
        }

        int index = indexMapping(row, col);
        return open[index];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // for corner case - row or col not in range [1, n]
        if (!isvalid(row) || !isvalid(col)) {
            throw new IllegalArgumentException();
        }

        int index = indexMapping(row, col);
        return ufWithoutBottom.find(0) == ufWithoutBottom.find(index);
        // return uf.connected(0, index);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return cntOpen;
    // The spec requires that all instance methods take constant time, so just maintain a cntOpen variable.
    //     int count = 0;
    //     for (int i = 1; i < open.length - 1; i++) {
    //         if (open[i])
    //         count++;
    //     }
    //     return count;
    }

    // does the system percolate?
    public boolean percolates() {
        int q = open.length - 1;
        return uf.find(0) == uf.find(q);
        // return uf.connected(0, q);
    }

    // test client (optional)
    // public static void main(String[] args){
    //
    // }
}
