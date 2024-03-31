import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root = null;
    private int size = 0;

    private class Node {
        private Point2D point;
        private Node left, right;
        private int level;
        private RectHV rect;

        private Node(Point2D point, int level, RectHV rect) {
            this.point = new Point2D(point.x(),
                                     point.y()); // Point2D is a mutable object, defensive copy
            this.left = null;
            this.right = null;
            this.level = level;
            this.rect = rect;
        }

    } // create node for the KdTree

    public boolean isEmpty() {
        return (size() == 0);
    } // is the tree empty?

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument");
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p) {
        if (x == null) return false;
        if (x.point.equals(p)) return true;

        if (x.level % 2 != 0) {
            if (p.x() < x.point.x()) return contains(x.left, p);
            else return contains(x.right, p);
        }
        else {
            if (p.y() < x.point.y()) return contains(x.left, p);
            else return contains(x.right, p);
        }
    }


    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument");
        root = insertPoint(root, p, 1, new RectHV(0, 0, 1, 1));
    } // insert a point into the KdTree

    private Node insertPoint(Node x, Point2D p, int level, RectHV rect) {
        if (x == null) {
            size++;
            return new Node(p, level, rect);
        }
        if (p.equals(x.point)) return x;

        if (level % 2 != 0) {
            if (p.x() < x.point.x()) {
                RectHV nextRect = new RectHV(rect.xmin(), rect.ymin(), x.point.x(), rect.ymax());
                x.left = insertPoint(x.left, p, level + 1, nextRect);
            }
            else {
                RectHV nextRect = new RectHV(x.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
                x.right = insertPoint(x.right, p, level + 1, nextRect);
            }
        }
        else {
            if (p.y() < x.point.y()) {
                RectHV nextRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.point.y());
                x.left = insertPoint(x.left, p, level + 1, nextRect);
            }
            else {
                RectHV nextRect = new RectHV(rect.xmin(), x.point.y(), rect.xmax(), rect.ymax());
                x.right = insertPoint(x.right, p, level + 1, nextRect);
            }
        }
        return x;
    }


    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1)); // Start drawing from the root node
    }

    private void draw(Node x, RectHV rect) {
        if (x == null) return;

        // Draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.point.draw(); // Draw the point

        // Draw splitting line
        if (x.level % 2 != 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(); // Reset to default pen radius
            StdDraw.line(x.point.x(), rect.ymin(), x.point.x(), rect.ymax()); // Vertical line
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(); // Reset to default pen radius
            StdDraw.line(rect.xmin(), x.point.y(), rect.xmax(), x.point.y()); // Horizontal line
        }

        // Recursively draw left and right subtrees
        if (x.left != null) {
            if (x.level % 2 != 0) {
                draw(x.left, new RectHV(rect.xmin(), rect.ymin(), x.point.x(),
                                        rect.ymax())); // Adjust rectangle for left subtree
            }
            else {
                draw(x.left, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(),
                                        x.point.y()));
            }
        }
        if (x.right != null) {
            if (x.level % 2 != 0) {
                draw(x.right, new RectHV(x.point.x(), rect.ymin(), rect.xmax(),
                                         rect.ymax()));
            }
            else {
                draw(x.right, new RectHV(rect.xmin(), x.point.y(), rect.xmax(),
                                         rect.ymax()));
            }
        }

    } // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("null argument");
        Queue<Point2D> pointQueue = new Queue<>();
        range(root, rect, pointQueue);
        return pointQueue;
    } // all points that are inside the rectangle (or on the boundary)

    private void range(Node x, RectHV rect, Queue<Point2D> pointQueue) {
        if (x == null) return;
        if (rect.contains(x.point)) pointQueue.enqueue(x.point);
        if (x.left != null && rect.intersects(x.left.rect)) {
            range(x.left, rect, pointQueue);
        }
        if (x.right != null && rect.intersects(x.right.rect)) {
            range(x.right, rect, pointQueue);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument");
        if (root == null) return null;
        return nearest(root, p, root.point);
    } // a nearest neighbor in the set to point p; null if the set is empty

    private Point2D nearest(Node x, Point2D p, Point2D closest) {
        if (x == null) return closest;
        if (p.distanceSquaredTo(closest) > p.distanceSquaredTo(x.point)) {
            closest = x.point;
        }
        Node nearSubtree, farSubtree;
        if ((x.level % 2 != 0 && p.x() < x.point.x()) || (x.level % 2 == 0
                && p.y() < x.point.y())) {
            nearSubtree = x.left;
            farSubtree = x.right;
        }
        else {
            nearSubtree = x.right;
            farSubtree = x.left;
        }
        // Recursively search the near subtree
        closest = nearest(nearSubtree, p, closest);

        // Pruning: check if it's necessary to search the far subtree
        if (farSubtree != null) {
            if (p.distanceSquaredTo(closest) > farSubtree.rect.distanceSquaredTo(p)) {
                closest = nearest(farSubtree, p, closest);
            }
        }
        return closest;
    }


    public static void main(String[] args) {

    }
}
