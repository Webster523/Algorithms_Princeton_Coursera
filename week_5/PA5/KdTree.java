/* *****************************************************************************
 *  Name:              Weixiao Chen
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (!contains(p)) {
            size++;
            root = insert(root, null, p, 0);
        }
    }

    // 0 -> even level -> use x-coordinate
    // 1 -> odd level -> use y-coordinate
    private Node insert(Node node, Node parent, Point2D p, int level) {
        // inserting the rectangle containing each point
        if (node == null) {
            RectHV rect = null;
            if (parent == null) { // must be the root node
                rect = new RectHV(0, 0, 1, 1);
            }
            else {
                Point2D parentP = parent.p;
                RectHV parentRect = parent.rect;
                double xmin, ymin, xmax, ymax;
                // The logic should be flipped here, since we don't do insert or contain operation here.
                // Instead, we just use the level to traverse back to know how *this* node goes to this path.
                if (level == 1) {
                    if (p.x() < parentP.x()) {
                        xmin = parentRect.xmin();
                        ymin = parentRect.ymin();
                        xmax = parentP.x();
                        ymax = parentRect.ymax();
                    }
                    else {
                        xmin = parentP.x();
                        ymin = parentRect.ymin();
                        xmax = parentRect.xmax();
                        ymax = parentRect.ymax();
                    }
                }
                else {
                    if (p.y() < parentP.y()) {
                        xmin = parentRect.xmin();
                        ymin = parentRect.ymin();
                        xmax = parentRect.xmax();
                        ymax = parentP.y();
                    }
                    else {
                        xmin = parentRect.xmin();
                        ymin = parentP.y();
                        xmax = parentRect.xmax();
                        ymax = parentRect.ymax();
                    }
                }
                rect = new RectHV(xmin, ymin, xmax, ymax);
            }
            return new Node(p, rect, level);
        }


        // recursively find the right place to insert
        int cmp;
        if (level == 0) {
            cmp = Double.compare(p.x(), node.p.x());
        }
        else {
            cmp = Double.compare(p.y(), node.p.y());
        }

        if (cmp < 0)
            node.lb = insert(node.lb, node, p, (level + 1) % 2);
        else
            node.rt = insert(node.rt, node, p, (level + 1) % 2);
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return contains(root, p, 0);
    }

    // 0 -> even level -> use x-coordinate
    // 1 -> odd level -> use y-coordinate
    private boolean contains(Node node, Point2D p, int level) {
        if (node == null)
            return false;
        if (node.p.equals(p))
            return true;

        int cmp;
        if (level == 0) {
            cmp = Double.compare(p.x(), node.p.x());
        }
        else {
            cmp = Double.compare(p.y(), node.p.y());
        }

        if (cmp < 0)
            return contains(node.lb, p, (level + 1) % 2);
        else
            return contains(node.rt, p, (level + 1) % 2);
    }

    // draw all points to standard draw
    public void draw() {
        if (isEmpty())
            return;

        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        Point2D starting = null, ending = null;
        double x1, y1, x2, y2;
        while (!queue.isEmpty()) {
            Node node = queue.dequeue();

            // draw point
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();

            // draw subdivisions
            StdDraw.setPenRadius();
            if (node.level == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                x1 = node.p.x();
                x2 = node.p.x();
                y1 = node.rect.ymin();
                y2 = node.rect.ymax();
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                x1 = node.rect.xmin();
                x2 = node.rect.xmax();
                y1 = node.p.y();
                y2 = node.p.y();
            }
            starting = new Point2D(x1, y1);
            ending = new Point2D(x2, y2);
            starting.drawTo(ending);


            if (node.lb != null)
                queue.enqueue(node.lb);
            if (node.rt != null)
                queue.enqueue(node.rt);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        if (isEmpty()) {
            return null;
        }

        Queue<Point2D> queue = new Queue<Point2D>();

        Queue<Node> helper = new Queue<>();

        Node node = null;
        helper.enqueue(root);
        while (!helper.isEmpty()) {
            node = helper.dequeue();
            if (rect.intersects(node.rect)) {
                if (rect.contains(node.p)) {
                    queue.enqueue(node.p);
                }
                if (node.lb != null) {
                    helper.enqueue(node.lb);
                }
                if (node.rt != null) {
                    helper.enqueue(node.rt);
                }
            }
        }
        return queue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        if (isEmpty()) {
            return null;
        }

        Point2D target = root.p, currentP = null;
        Node node = null;
        Queue<Node> queue = new Queue<>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            node = queue.dequeue();
            if (!(p.distanceSquaredTo(target) < node.rect.distanceSquaredTo(p))) {
                currentP = node.p;
                if (p.distanceSquaredTo(currentP) < p.distanceSquaredTo(target)) {
                    target = currentP;
                }
                if (node.lb != null && node.rt != null) {
                    // if (node.level == 0) {
                    //     if (p.x() < currentP.x()) {
                    //         queue.enqueue(node.lb);
                    //         queue.enqueue(node.rt);
                    //     }
                    //     else {
                    //         queue.enqueue(node.rt);
                    //         queue.enqueue(node.lb);
                    //     }
                    // }
                    // else {
                    //     if (p.y() < currentP.y()) {
                    //         queue.enqueue(node.lb);
                    //         queue.enqueue(node.rt);
                    //     }
                    //     else {
                    //         queue.enqueue(node.rt);
                    //         queue.enqueue(node.lb);
                    //     }
                    // }
                    queue.enqueue(node.lb);
                    queue.enqueue(node.rt);
                }
                else if (node.lb != null)
                    queue.enqueue(node.lb);
                else if (node.rt != null)
                    queue.enqueue(node.rt);
            }
        }
        return target;
    }

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private final int level;

        public Node(Point2D p, RectHV rect, int level) {
            this.p = p;
            this.rect = rect;
            this.lb = null;
            this.rt = null;
            this.level = level;
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        kdtree.insert(new Point2D(1.0, 0.125));
        kdtree.insert(new Point2D(0.25, 0.875));
        kdtree.insert(new Point2D(0.625, 0.0));
        kdtree.insert(new Point2D(0.0, 0.5));
        kdtree.insert(new Point2D(0.75, 0.25));
        kdtree.range(new RectHV(0.125, 0.625, 0.875, 1.0));
        // kdtree.insert(new Point2D(0.975528, 0.345492));
        // kdtree.insert(new Point2D(0.206107, 0.904508));
        // kdtree.insert(new Point2D(0.500000, 0.000000));
        // kdtree.insert(new Point2D(0.024472, 0.654508));
        // kdtree.insert(new Point2D(0.500000, 1.000000));
        // kdtree.draw();
    }
}
