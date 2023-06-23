package com.gamefocal.rivenworld.game.ai.path;

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

    public static PriorityQueue<WorldCell> getPriorityQueue(Location start, Location goal) {
        return new PriorityQueue<WorldCell>((o1, o2) -> {
            double g1 = o1.getCenterInGameSpace(true).dist(start);
            double h1 = o1.getCenterInGameSpace(true).dist(goal);
            double f1 = h1 + g1;

            double g2 = o2.getCenterInGameSpace(true).dist(start);
            double h2 = o2.getCenterInGameSpace(true).dist(goal);
            double f2 = h2 + g2;

            if (f1 > f2) {
                return +1;
            } else if (f1 < f2) {
                return -1;
            } else {
                return 0;
            }
        });
    }

    public static PriorityQueue<WorldCell> getClosestToGoal(Location start, Location goal, Collection<WorldCell> cells) {
        PriorityQueue<WorldCell> worldCells = getPriorityQueue(start, goal);
        worldCells.addAll(cells);
        return worldCells;
    }

    public static List<WorldCell> findPath(WorldCell start, WorldCell goal, AiPathValidator validator) {
        return findPath(start, goal, validator, new ArrayList<>(), 0);
    }

    public static List<WorldCell> findPath(WorldCell start, WorldCell goal, AiPathValidator validator, ArrayList<WorldCell> limit, float dist) {
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

            if (current.equals(goal) || (dist > 0 && current.getCenterInGameSpace(true).dist(goal.getCenterInGameSpace(true)) < dist)) {
                return reconstructPath(cameFrom, current);
            }

            for (WorldCell neighbor : current.getNeighbors(true)) {

                // Standard collision check for cell
                if (closedSet.contains(neighbor) || !neighbor.isCanTraverse()) {
                    continue;
                }

                if (limit.size() > 0 && !limit.contains(neighbor)) {
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

        return null;
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
