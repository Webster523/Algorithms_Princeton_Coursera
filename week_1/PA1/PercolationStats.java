/* *****************************************************************************
 *  Name:              Weixiao Chen
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double standardDeviation;
    private final double confidenceLo;
    private final double condifenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        double[] fraction = new double[trials];
        int row = 0, col = 0;
        int cntObject = n * n;
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                 try {
                    do {
                        row = StdRandom.uniform(n + 1);
                        col = StdRandom.uniform(n + 1);
                    } while (percolation.isOpen(row, col));
                    percolation.open(row, col);
                }
                catch (IllegalArgumentException e) {
                    continue;
                }
            }
            fraction[i] = 1.0 * percolation.numberOfOpenSites() / cntObject;
        }

        this.mean = StdStats.mean(fraction);
        this.standardDeviation = StdStats.stddev(fraction);
        this.confidenceLo = this.mean - CONFIDENCE_95 * this.standardDeviation / (Math.sqrt(trials));
        this.condifenceHi = this.mean + CONFIDENCE_95 * this.standardDeviation / (Math.sqrt(trials));
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.standardDeviation;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.condifenceHi;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats p = new PercolationStats(n, trials);
        StdOut.printf("%-23s = %f\n", "mean", p.mean());
        StdOut.printf("%-23s = %f\n", "stddev", p.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]", p.confidenceLo(), p.confidenceHi());
    }
}
