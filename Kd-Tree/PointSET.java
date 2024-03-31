import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;


public class PointSET {
    private SET<Point2D> points;

    public PointSET() {
        points = new SET<>();
    } // construct an empty set of points

    public boolean isEmpty() {
        return points.isEmpty();
    } // is the set empty?

    public int size() {
        return points.size();
    } // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument");
        points.add(p);

    } // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        return points.contains(p);
    } // does the set contain point p?

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        for (Point2D p : points) {
            StdDraw.point(p.x(), p.y());
        }
    } // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("null argument");
        Queue<Point2D> pointRange = new Queue<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                pointRange.enqueue(p);
            }
        }
        return pointRange;
    } // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument");
        if (points.isEmpty()) return null;
        double distance = Double.POSITIVE_INFINITY;
        Point2D target = null;
        for (Point2D point : points) {
            double temp = p.distanceSquaredTo(point);
            if (temp < distance) {
                distance = temp;
                target = point;
            }
        }
        return target;
    } // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        PointSET points = new PointSET();
        points.insert(new Point2D(0.3, 0.5));
        points.insert(new Point2D(0.2, 0.6));
        points.draw();
    } // unit testing of the methods (optional)
}
