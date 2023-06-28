package com.gamefocal.rivenworld.game.ai.path;

import com.gamefocal.rivenworld.game.util.WorldDirection;

import java.util.*;

public class AStarSearch {

    private PriorityQueue<Node> open = new PriorityQueue<>();
    private LinkedList<Node> closed = new LinkedList<>();
    private LinkedList<Node> path = new LinkedList<>();
    private Node now;
    private WorldCell start;
    private WorldCell target;
    private List<WorldCell> limitedSearchArea = new ArrayList<>();
    private int timeoutInMilli = 0;
    private AiPathValidator validator;

    public AStarSearch(WorldCell start, WorldCell target, AiPathValidator validator, List<WorldCell> limitedArea, int timeoutInMilli) {
        this.start = start;
        this.target = target;
        this.open = new PriorityQueue<>((o1, o2) -> {
            if (o1.f > o2.f) {
                return +1;
            } else if (o1.f < o2.f) {
                return -1;
            } else {
                return 0;
            }
        });
        this.limitedSearchArea = limitedArea;
        this.timeoutInMilli = timeoutInMilli;
        this.validator = validator;
    }

    public LinkedList<WorldCell> findPathTo() {

        long startAt = System.currentTimeMillis();

        this.now = new Node(null, this.start, 0, 0, null, 0);
        this.open.add(this.now);

        boolean foundGoal = false;
        while (this.open.size() > 0) {
            // As long as we have an open list
            Node n = this.open.poll();
            this.closed.add(n);
            this.now = n;

            // Timeout
            if ((System.currentTimeMillis() - startAt) >= this.timeoutInMilli) {
                return null;
            }

            if (n.tile.equals(this.target)) {
                // Is the target node
                foundGoal = true;
                break;
            }

            /*
             * Check the neighbors and add them to the list.
             * */
            for (WorldCell t : n.tile.getNeighbors(false)) {
                if (t != null) {
                    Node nn = new Node(
                            this.now,
                            t,
                            this.now.tile.distanceTo(this.start),
                            this.now.tile.distanceTo(this.target),
                            null,
                            this.now.totalCost
                    );

                    if (nn.tile.equals(this.target)) {
                        this.open.add(nn);
                        continue;
                    }

                    // Check if it is in the area
                    if (this.limitedSearchArea.size() > 0 && !this.limitedSearchArea.contains(nn.tile)) {
                        continue;
                    }

                    if (!this.open.contains(nn) && !this.closed.contains(nn) || nn.totalCost < this.now.totalCost) {

                        if (this.validator != null && !this.validator.check(nn.tile)) {
                            continue;
                        }

                        if (t.canTravelFromCell(this.now.tile, null)) {
                            this.open.add(nn);
                        }
                    }
                }
            }
        }

//        if (!foundGoal) {
////            System.out.println("No Goal found :(");
//            return null;
//        }

        LinkedList<WorldCell> route = new LinkedList<>();
        while (this.now.parent != null) {
            route.add(this.now.tile);
            this.now = this.now.parent;
        }

        if (route.size() == 0) {
            return null;
        }

// Reverse the route so it starts from the initial cell
        Collections.reverse(route);

        return route;

//        LinkedList<WorldCell> route = new LinkedList<>();
//        while (this.now.parent != null) {
//            route.add(this.now.tile);
//            this.now = this.now.parent;
//        }
//
//        if (route.size() == 0) {
//            return null;
//        }
//
//        return route;
    }

    /*
     * Helper Classes
     * */

    // Node class for convienience
    public static class Node {
        public WorldDirection direction;
        public Node parent;
        public WorldCell tile;
        public double g; // Distance from starting node
        public double h; // Distance from the target location
        public double f; // g + h
        public double totalCost = 0;
        // Choose lowset f cost
        // If f cost are all the same, use the h cost
        // If a f cost goes up,

        Node(Node parent, WorldCell tile, double g, double h, WorldDirection direction, double costSoFar) {
            this.parent = parent;
            this.tile = tile;
            this.g = g;
            this.h = h;
            this.f = this.g + this.h;
            this.direction = direction;
            this.totalCost = costSoFar + this.f;
        }

        @Override
        public boolean equals(Object obj) {
            Node n = (Node) obj;
            return n.tile.equals(this.tile);
        }
    }

    public static abstract class PathFindingResult implements Runnable {

        private boolean pathFound = false;

        private LinkedList<WorldCell> path = null;

        public PathFindingResult() {
        }

        public boolean isPathFound() {
            return pathFound;
        }

        public void setPathFound(boolean pathFound) {
            this.pathFound = pathFound;
        }

        public LinkedList<WorldCell> getPath() {
            return path;
        }

        public void setPath(LinkedList<WorldCell> path) {
            this.path = path;
        }
    }

}
