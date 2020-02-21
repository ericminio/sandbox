package ericminio.katas.drone;

import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class DroneTest {

    @Test
    public void isObviousForOneTarget() {
        Drone drone = new Drone();
        drone.setTargets(new Point[]{ new Point(15, 42) });
        Point[] path = drone.findOptimumPath();

        assertThat(path, equalTo(new Point[]{ new Point(15, 42) }));
    }
    @Test
    public void isObviousForTwoTargets() {
        Drone drone = new Drone();
        drone.setTargets(new Point[]{ new Point(1, 1), new Point(2, 2) });
        Point[] path = drone.findOptimumPath();

        assertThat(path, equalTo(new Point[]{ new Point(1, 1), new Point(2, 2) }));
    }

    class Drone {

        private Point[] targets;

        public void setTargets(Point[] targets) {
            this.targets = targets;
        }

        public Point[] findOptimumPath() {
            List<Point> path = new ArrayList<>();

            path.add(targets[0]);
            if (targets.length > 1) {
                path.add(targets[1]);
            }

            return path.toArray(new Point[0]);
        }

        public Point[] getTargets() {
            return targets;
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

        public boolean equals(Object o) {
            Point other = (Point) o;

            return getX() == other.getX() && getY() == other.getY();
        }
    }
}
