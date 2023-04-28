package com.gamefocal.rivenworld.game.ai.path;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.PlayerService;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class LocationPathFinder {

    private WorldGrid grid;
    private PriorityQueue<Node> open = new PriorityQueue<>();
    private LinkedList<Node> closed = new LinkedList<>();
    private LinkedList<Node> path = new LinkedList<>();
    private Node now;
    private Location start;
    private Location target;

    private WorldCell startCell;
    private WorldCell targetCell;

    public LocationPathFinder(WorldGrid grid) {
        this.grid = grid;

        this.open = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.f > o2.f) {
                    return +1;
                } else if (o1.f < o2.f) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    public LinkedList<WorldCell> findPathTo(Location start, Location target) {
        this.target = target;

        this.startCell = this.grid.getCellFromGameLocation(start);
        this.targetCell = this.grid.getCellFromGameLocation(target);

        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            connection.drawDebugLine(Color.GREEN, start.cpy().addZ(200), target.cpy().addZ(200), 2);
        }

        this.now = new Node(null, this.startCell, 0, 0, 0);
        this.open.add(this.now);

        boolean foundGoal = false;
        while (this.open.size() > 0) {
            // As long as we have an open list
            Node n = this.open.poll();
            this.closed.add(n);
            this.now = n;

//            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//                connection.drawDebugBox(Color.GRAY, n.tile.getBoundingBox(), 4);
//            }

            if (n.tile.equals(this.targetCell)) {
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
                            this.now.tile.getCenterInGameSpace(true).dist(this.start),
                            this.now.tile.getCenterInGameSpace(true).dist(this.target),
//                            LocationUtil.rawDistance(this.now.tile.getLocation(), this.start),
//                            LocationUtil.rawDistance(this.now.tile.getLocation(), this.target),
                            this.now.totalCost
                    );

                    if (nn.tile.equals(this.targetCell)) {
                        this.open.add(nn);
                        continue;
                    }

                    if (!this.open.contains(nn) && !this.closed.contains(nn) || nn.totalCost < this.now.totalCost) {

                        /*
                         * Elevation change
                         * */
                        float nh = this.grid.getWorld().getRawHeightmap().getHeightFromLocation(this.now.tile.getCenterInGameSpace(false));
                        float th = this.grid.getWorld().getRawHeightmap().getHeightFromLocation(t.getCenterInGameSpace(false));

                        if (t.isCanTraverse()) {

                            float slope = Math.abs(th - nh);

                            if (slope > 200) {

                                System.out.println("To Steep: " + slope);

                                // Can not traverse this height
                                continue;
                            }

//                            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//                                connection.drawDebugBox(Color.GREEN, nn.tile.getBoundingBox(), 4);
//                            }

                            this.open.add(nn);
                        } else {
                            System.out.println("Is Blocked");
                        }
                    }
                }
            }
        }

        if (!foundGoal) {
            System.out.println("No Goal found :(");
            return null;
        }

        LinkedList<WorldCell> route = new LinkedList<>();
        while (this.now.parent != null) {
            route.add(this.now.tile);
            this.now = this.now.parent;
        }

        return route;
    }

    // Node class for convienience
    public static class Node {
        public Node parent;
        public WorldCell tile;
        public double g; // Distance from starting node
        public double h; // Distance from the target location
        public double f; // g + h
        public double totalCost = 0;
        // Choose lowset f cost
        // If f cost are all the same, use the h cost
        // If a f cost goes up,

        Node(Node parent, WorldCell tile, double g, double h, double costSoFar) {
            this.parent = parent;
            this.tile = tile;
            this.g = g;
            this.h = h;
            this.f = this.g + this.h;
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
