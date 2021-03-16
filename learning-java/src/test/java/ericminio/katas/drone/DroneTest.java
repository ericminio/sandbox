package ericminio.katas.drone;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class DroneTest {

    @Test
    public void startingPosition() {
        assertThat(new Drone().position(), equalTo(new Point(0, 0)));
    }

    @Test
    public void canFindPathToOneTarget() {
        Drone drone = new Drone();
        drone.setTargets(new ArrayList<Point>() {{ add(new Point(15, 42)); }} );
        Point[] path = drone.findShortestPath();

        assertThat(path, equalTo(new Point[]{ new Point(0, 0), new Point(15, 42) }));
    }

    @Test
    public void usesManhattanDistance() {
        assertThat(Distance.between(new Point(0, 0), new Point(-2, -3)), equalTo(5));
    }
    @Test
    public void knowsTheLengthOfOnePath() {
        assertThat(new Drone().pathLength(new Point[]{ new Point(0, 0), new Point(1, 1), new Point(2, 0) }), equalTo(4));
    }

    @Test
    public void visitsTheClosestPointFirst() {
        Drone drone = new Drone();
        drone.setTargets(new ArrayList<Point>() {{ add(new Point(2, 2)); add(new Point(1, 1)); }} );
        Point[] path = drone.findShortestPath();

        assertThat(path, equalTo(new Point[]{ new Point(0, 0), new Point(1, 1), new Point(2, 2) }));
    }

    @Test
    public void reConsiderWhatIsTheClosestPointAlongTheTrip() {
        Drone drone = new Drone();
        drone.setTargets(new ArrayList<Point>() {{
                add(new Point(-1, 1));
                add(new Point(-2, 2));
                add(new Point(1, 1));
                add(new Point(2, 2));
            }});
        Point[] path = drone.findShortestPath();

        assertThat(path, equalTo(new Point[]{
                new Point(0, 0),
                new Point(-1, 1), new Point(-2, 2),
                new Point(1, 1), new Point(2, 2)
        }));
    }

    class Drone {

        private Point position;
        private List<Point> targets;

        public Drone() {
            position = new Point(0, 0);
        }

        public void setTargets(List<Point> targets) {
            this.targets = targets;
        }

        public Point[] findShortestPath() {
            List<Point> path = new ArrayList<>();
            path.add(position);

            while (targets.size() > 0) {
                List<Vector> vectors = new ArrayList<>();
                for (Point p : targets) {
                    vectors.add(new Vector(position(), p));
                }
                vectors.sort(Comparator.comparingInt(Vector::size));
                Point destination = vectors.get(0).getDestination();
                path.add(destination);
                setPosition(destination);
                targets.remove(destination);
            }

            return path.toArray(new Point[0]);
        }

        private void setPosition(Point p) {
            this.position = p;
        }

        public Point position() {
            return position;
        }

        public int pathLength(Point[] points) {
            int length = 0;

            Point current = position();
            for (Point p:points) {
                length += Distance.between(current, p);
                current = p;
            }
            return length;
        }
    }
    class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String toString() {
            return "Point: x="+getX() + ", y="+getY();
        }

        public boolean equals(Object o) {
            Point other = (Point) o;

            return getX() == other.getX() && getY() == other.getY();
        }
    }
    class Vector {

        private final Point origin;
        private final Point destination;

        public Vector(Point origin, Point destination) {
            this.origin = origin;
            this.destination = destination;
        }

        public int size() {
            return Distance.between(origin, destination);
        }

        public Point getDestination() {
            return destination;
        }
    }
    static class Distance {

        public static int between(Point a, Point b) {
            return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
        }
    }
}
