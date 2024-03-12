import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("null argument"); // check invalid argument
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException("null point");
        } // check whether array element is null

        Point[] copyPoints = points.clone(); // duplicate the array to cook it
        Arrays.sort(copyPoints); // sort the array to make sure duplicate points are side-by-side

        if (copyPoints.length > 1) {
            for (int n = 0; n < copyPoints.length - 1; n++) {
                if (copyPoints[n].compareTo(copyPoints[n + 1]) == 0) {
                    throw new IllegalArgumentException("duplicate points");
                }
            }
        } // check duplicate points

        ArrayList<LineSegment> segHolder = new ArrayList<>();
        Point[] tempPoints = copyPoints.clone();
        if (copyPoints.length >= 4) {
            for (Point point : copyPoints) {
                Arrays.sort(tempPoints, point.slopeOrder());
                getSegments(tempPoints, point, segHolder);
            } // for each point, check whether exist at least 3 other points that are collinear with it.
        }
        LineSegment[] arr = new LineSegment[segHolder.size()];
        segments = segHolder.toArray(arr);
    }

    public int numberOfSegments() {
        return segments.length;
    }      // the number of line segments

    public LineSegment[] segments() {
        return segments.clone();
    }      // the line segments

    private static void getSegments(Point[] points, Point p, ArrayList<LineSegment> segHolder) {
        int start = 1;
        double slope = p.slopeTo(points[1]);
        int n = points.length;
        for (int i = 2; i < n; i++) {
            double tempSlope = p.slopeTo(points[i]);
            if (tempSlope != slope) {
                if (i - start >= 3) {
                    Point[] segmentArray = createSegment(points, p, start, i);
                    if (segmentArray[0].compareTo(p) == 0) {
                        LineSegment segment = new LineSegment(segmentArray[0], segmentArray[1]);
                        segHolder.add(segment);
                    }
                }
                start = i;
                slope = tempSlope;
            }

        }
        // in situations where the last element in the points array is collinear with its predecessors,
        // we can't partition the elements with the above if statement.
        if (n - start >= 3) {
            Point[] segmentArray = createSegment(points, p, start, n);
            if (segmentArray[0].compareTo(p) == 0) {
                LineSegment segment = new LineSegment(segmentArray[0], segmentArray[1]);
                segHolder.add(segment);
            }
        }


    }

    private static Point[] createSegment(Point[] points, Point p, int start, int end) {
        ArrayList<Point> collinearPoints = new ArrayList<>();
        collinearPoints.add(p);
        collinearPoints.addAll(Arrays.asList(points).subList(start, end));
        collinearPoints.sort(Point::compareTo);
        Point[] segment = new Point[] {
                collinearPoints.get(0),
                collinearPoints.get(collinearPoints.size() - 1)
        };
        return segment;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        FastCollinearPoints collinear = new FastCollinearPoints(points);

        // print and draw the line segments
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}


