/* *****************************************************************************
 *  Name:              Weixiao Chen
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (!contains(p))
            set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        double xmin = rect.xmin(), xmax = rect.xmax();
        double ymin = rect.ymin(), ymax = rect.ymax();

        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D p : set) {
            double x = p.x(), y = p.y();
            if ((xmin <= x && x <= xmax) && (ymin <= y && y <= ymax))
                queue.enqueue(new Point2D(x, y));
        }
        return queue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (set.isEmpty())
            return null;
        Point2D champion = null;
        for (Point2D original : set) {
            if (champion == null || p.distanceSquaredTo(original) < p.distanceSquaredTo(champion))
                champion = original;
        }
        return champion;
    }

    // unit testing of the methods (optional)
    // public static void main(String[] args) {
    //
    // }
}
