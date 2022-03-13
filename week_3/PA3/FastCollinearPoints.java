/* *****************************************************************************
 *  Name:              Weixiao Chen
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lineSegments;

    // Note: The WeightedQuickUnionUF - an implementation of union-set data structure cannot be used as helper,
    // since the transitive does not hold for the collinear relation.
    // (i.e., p is collinear to q and q is collinear to w. ×××-> p is collinear to w.)

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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

        // data type should not mutate argument
        Point[] copy = Arrays.copyOf(points, size);
        Arrays.sort(copy);

        lineSegments = new ArrayList<LineSegment>();

        // The loop variable goes from 0 to size - 4, since we only consider line segment containing at least four points.
        for (int i = 0; i <= size - 4; i++) {
            // Step 1 - Take each point as the origin.
            Point origin = copy[i];

            // The collinear relation should be symmetric.
            // I.e., p is collinear to q, which also means q is collinear to p.
            // Therefore, we only need to check whether p is collinear to q.
            // And we won't look back to check whether q is collinear to p, which makes it much more complicated.
            Point[] left = new Point[size - i - 1];
            int index = 0;
            for (int j = i + 1; j < size; j++) {
                left[index++] = copy[j];
            }

            // Step 2 - For each other point q, determine the slope it makes with p.
            // Step 3 - Sort the points according to the slopes they makes with p.
            Arrays.sort(left, origin.slopeOrder());

            // Step 4 - Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p.
            // If so, these points, together with p, are collinear.
            double slope = origin.slopeTo(left[0]);
            int count = 1;
            for (int j = 1; j < left.length; j++) {
                if (origin.slopeTo(left[j]) == slope) {
                    count++;

                    // This must be a corner case - [-1.0, 0.0, 0.0, 0.0].
                    // The last element in the left array might be the last point in *this* line segment.
                    // If we don't check this, this segment is lost.
                    if (j == left.length - 1 && count >= 3) {

                        // Checking whether this line segment origin -> left[j] is a subsegment.
                        // We just need to traverse the sorted copy array reversely from the point before origin to the first element,
                        // during which we check if there is a point whose slope to origin is the same as the current slope value.
                        boolean isMax = true;
                        for (int k = i - 1; k >= 0; k--) {
                            if (copy[k].slopeTo(origin) == slope) {
                                isMax = false;
                                break;
                            }
                        }

                        // If so, origin -> left[j] must be a subsegment and we don't need to add it to our arraylist.
                        // Otherwise, origin -> left[j] must be the maximal line segment. Add it!!!!
                        if (isMax) {
                            lineSegments.add(new LineSegment(origin, left[j]));
                        }
                    }
                }
                else {
                    if (count >= 3) {
                        boolean isMax = true;
                        for (int k = i - 1; k >= 0; k--) {
                            if (copy[k].slopeTo(origin) == slope) {
                                isMax = false;
                                break;
                            }
                        }
                        // Possible test case - [-1.0, 0.0, 0.0, 0.0, 2.0].
                        // When this line of code is executed, the pointer j has already gone beyond the last collinear point 1 unit.
                        // Therefore, the line segment is origin -> left[j - 1] this time rather than origin -> left[j].
                        if (isMax) {
                            lineSegments.add(new LineSegment(origin, left[j - 1]));
                        }
                    }
                    slope = origin.slopeTo(left[j]);
                    count = 1;
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
        // return lineSegments;

        // defensive copy
        int size = lineSegments.size();
        LineSegment[] copy = new LineSegment[size];
        for (int i = 0; i < size; i++) {
            copy[i] = lineSegments.get(i);
        }
        return copy;
    }

}