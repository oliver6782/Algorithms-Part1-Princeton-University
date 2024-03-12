import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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
        if (copyPoints.length >= 4) {
            Point[] sample = new Point[4];
            for (int j = 0; j < copyPoints.length - 3; j++) {
                sample[0] = copyPoints[j];
                for (int k = j + 1; k < copyPoints.length - 2; k++) {
                    sample[1] = copyPoints[k];
                    for (int q = k + 1; q < copyPoints.length - 1; q++) {
                        sample[2] = copyPoints[q];
                        for (int m = q + 1; m < copyPoints.length; m++) {
                            sample[3] = copyPoints[m];
                            if (collinear(sample)) {
                                LineSegment segment = pToq(sample);
                                segHolder.add(segment);
                            }
                        }
                    }
                }
            }
        }
        LineSegment[] arr = new LineSegment[segHolder.size()];
        segments = segHolder.toArray(arr);

    }

    // helpers
    private boolean collinear(Point[] points) {
        double slope1 = points[0].slopeTo(points[1]);
        double slope2 = points[0].slopeTo(points[2]);
        double slope3 = points[0].slopeTo(points[3]);
        return slope1 == slope2 && slope2 == slope3;
    }

    private LineSegment pToq(Point[] points) {
        Arrays.sort(points);
        return new LineSegment(points[0], points[3]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }

    // client test
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

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
