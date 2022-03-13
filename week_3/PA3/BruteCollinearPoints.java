/* *****************************************************************************
 *  Name:              Weixiao Chen
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // test for corner case 1 - the argument to the constructor is null
        if (points == null) {
            throw new IllegalArgumentException();
        }

        int size = points.length;
        // test for corner case 2 - any point in the array is null
        for (int i = 0; i < size; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        // test for corner case 3 - the argument contains a repeated point
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        Point[] copy = new Point[size];
        for (int i = 0; i < size; i++)
            copy[i] = points[i];

        Arrays.sort(copy);    // to make sure all points appear in onder p->q->r->s

        lineSegments = new ArrayList<LineSegment>();
        double slope1, slope2, slope3;
        for (int i = 0; i < size - 3; i++) {
            for (int j = i + 1; j < size - 2; j++) {
                for (int k = j + 1; k < size - 1; k++) {
                    for (int l = k + 1; l < size; l++) {
                        slope1 = copy[i].slopeTo(copy[j]);
                        slope2 = copy[i].slopeTo(copy[k]);
                        slope3 = copy[i].slopeTo(copy[l]);
                        if (slope1 == slope2 && slope1 == slope3) {
                            lineSegments.add(new LineSegment(copy[i], copy[l]));
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        // a super bad design - how dare you return a reference to the internal private data structure back to user???
        // return lineSegements;

        // defensive copy
        int size = lineSegments.size();
        LineSegment[] copy = new LineSegment[size];
        for (int i = 0; i < size; i++) {
            copy[i] = lineSegments.get(i);
        }
        return copy;
    }

}
