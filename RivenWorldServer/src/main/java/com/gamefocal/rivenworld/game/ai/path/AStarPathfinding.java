package com.gamefocal.rivenworld.game.ai.path;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.WorldDirection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class AStarPathfinding {

    public static ConcurrentHashMap<UUID, Integer> pathFindingAttempts = new ConcurrentHashMap<>();

    public static void asyncFindPath(WorldCell start, WorldCell goal, AiPathResult promise) {
        new Thread(() -> {
            List<WorldCell> path = findPath(start, goal, null);
            promise.onPath(path);
        }).start();
    }

    public static void asyncFindPath(WorldCell start, WorldCell goal, AiPathResult promise, AiPathValidator validator) {
        new Thread(() -> {
            List<WorldCell> path = findPath(start, goal, validator);
            promise.onPath(path);
        }).start();
    }

    public static Vector3 VFH(Location currentLoc, Vector3 velocity, Vector3 target, double entitySpeed) {

        // Parameters
        float minSpeed = 0; // Minimum speed
        float maxSpeed = (float) (entitySpeed * 2); // Maximum speed
        float maxTurn = (float) (Math.PI / 2); // Maximum turn rate in radians (45 degrees)
        int numSamples = 30; // Number of samples

        // Generate candidate velocities
        Vector3[] candidates = new Vector3[numSamples];
        for (int i = 0; i < numSamples; i++) {
            float speed = minSpeed + (maxSpeed - minSpeed) * (float) i / (numSamples - 1);
            float turn = -maxTurn + 2 * maxTurn * (float) i / (numSamples - 1);
            candidates[i] = new Vector3((float) (velocity.x * Math.cos(turn) - velocity.y * Math.sin(turn)),
                    (float) (velocity.x * Math.sin(turn) + velocity.y * Math.cos(turn)),
                    0).scl(speed);
        }

        // Predict and evaluate each candidate
        double[] costs = new double[numSamples];
        for (int i = 0; i < numSamples; i++) {
            // Predict the new position
            Vector3 newPosition = currentLoc.toVector().mulAdd(candidates[i], 100);

            // Check if the entity can traverse here
            WorldCell cell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(Location.fromVector(newPosition));

            // Calculate cost: distance to target + penalty for non-traversable cell
            double cost = currentLoc.dist(Location.fromVector(target));
            if (!cell.isCanTraverse()) {
                cost += 1000; // Large penalty for non-traversable cell
            }
            costs[i] = cost;
        }

        // Choose the candidate with the lowest cost
        int bestIndex = 0;
        for (int i = 1; i < numSamples; i++) {
            if (costs[i] < costs[bestIndex]) {
                bestIndex = i;
            }
        }

        return candidates[bestIndex];
    }

    public static List<WorldCell> findPath(WorldCell start, WorldCell goal, AiPathValidator validator) {
        long started = System.currentTimeMillis();
        Set<WorldCell> openSet = new HashSet<>();
        Set<WorldCell> closedSet = new HashSet<>();
        Map<WorldCell, WorldCell> cameFrom = new HashMap<>();

        Map<WorldCell, Float> gScore = new HashMap<>();
        gScore.put(start, 0f);

        Map<WorldCell, Float> fScore = new HashMap<>();
        fScore.put(start, heuristicCostEstimate(start, goal));

        openSet.add(start);

        while (!openSet.isEmpty()) {

            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - started) >= 15) {
                return null;
            }

            WorldCell current = getCellWithLowestFScore(openSet, fScore);

            openSet.remove(current);
            closedSet.add(current);

            if (current == null) {
                break;
            }

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            for (WorldCell neighbor : current.getNeighbors(true)) {

                // Standard collision check for cell
                if (closedSet.contains(neighbor) || !neighbor.isCanTraverse()) {
                    continue;
                }

                if (!neighbor.canTravelFromCell(current, validator)) {
                    continue;
                }

//                float myHeight = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(current.getCenterInGameSpace(true));
//                float nHeight = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(neighbor.getCenterInGameSpace(true));
//
//                float slope = Math.abs(nHeight - myHeight);
//
//                // Prevent steep slope
//                if (slope > 15) {
//                    continue;
//                }
//
//                // Prevent below sea level
//                if (neighbor.getCenterInGameSpace(true).getZ() <= 3000) {
//                    continue;
//                }
//
//                // Check the validator
//                if (validator != null && !validator.check(neighbor)) {
//                    continue;
//                }

                float tentativeGScore = gScore.get(current) + distanceBetween(current, neighbor);

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if (tentativeGScore >= gScore.get(neighbor)) {
                    continue;
                }

                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, gScore.get(neighbor) + heuristicCostEstimate(neighbor, goal));
            }
        }

        return null;
    }

    public static List<WorldCell> findPathToClosestCell(WorldCell start, WorldCell goal, AiPathValidator validator) {
        long started = System.currentTimeMillis();
        Set<WorldCell> openSet = new HashSet<>();
        Set<WorldCell> closedSet = new HashSet<>();
        Map<WorldCell, WorldCell> cameFrom = new HashMap<>();

        Map<WorldCell, Float> gScore = new HashMap<>();
        gScore.put(start, 0f);

        Map<WorldCell, Float> fScore = new HashMap<>();
        fScore.put(start, heuristicCostEstimate(start, goal));

        openSet.add(start);

        WorldCell closestCell = start; // Add this line to track closest traversable cell

        while (!openSet.isEmpty()) {

            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - started) >= 15) {
                return null;
            }

            WorldCell current = getCellWithLowestFScore(openSet, fScore);

            openSet.remove(current);
            closedSet.add(current);

            if (current == null) {
                break;
            }

            // Check if current cell is closer to the goal and is traversable, if so update closestCell
            if (current.isCanTraverse() && heuristicCostEstimate(current, goal) < heuristicCostEstimate(closestCell, goal)) {
                closestCell = current;
            }

            for (WorldCell neighbor : current.getNeighbors(true)) {

                // Standard collision check for cell
                if (closedSet.contains(neighbor) || !neighbor.isCanTraverse()) {
                    continue;
                }

                if (!neighbor.canTravelFromCell(current, validator)) {
                    continue;
                }

                float tentativeGScore = gScore.get(current) + distanceBetween(current, neighbor);

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if (tentativeGScore >= gScore.get(neighbor)) {
                    continue;
                }

                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, gScore.get(neighbor) + heuristicCostEstimate(neighbor, goal));
            }
        }

        // If the goal is traversable and was reached, return path to goal
        if (goal.isCanTraverse() && cameFrom.containsKey(goal)) {
            return reconstructPath(cameFrom, goal);
        }

        // Otherwise, return path to the closest traversable cell
        return reconstructPath(cameFrom, closestCell);
    }

    private static List<WorldCell> getTraversableCellsInRadius(WorldCell center, int radius, WorldGrid grid) {
        List<WorldCell> cells = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                WorldCell cell = grid.get(center.getX() + dx, center.getY() + dy);

                if (cell != null && cell.isCanTraverse()) {
                    cells.add(cell);
                }
            }
        }

        return cells;
    }

    private static float heuristicCostEstimate(WorldCell start, WorldCell goal) {
        return start.getCenterInGameSpace(false).dist(goal.getCenterInGameSpace(false));
    }

    private static WorldCell getCellWithLowestFScore(Set<WorldCell> openSet, Map<WorldCell, Float> fScore) {
        WorldCell lowest = null;
        for (WorldCell cell : openSet) {
            if (lowest == null || fScore.get(cell) < fScore.get(lowest)) {
                lowest = cell;
            }
        }
        return lowest;
    }

    private static List<WorldCell> reconstructPath(Map<WorldCell, WorldCell> cameFrom, WorldCell current) {
        List<WorldCell> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }

        Collections.reverse(path);
        return path;
    }

    private static float distanceBetween(WorldCell current, WorldCell neighbor) {
        return current.getCenterInGameSpace(false).dist(neighbor.getCenterInGameSpace(false));
    }

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
}
