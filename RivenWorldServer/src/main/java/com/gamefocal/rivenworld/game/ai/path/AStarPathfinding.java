package com.gamefocal.rivenworld.game.ai.path;

import com.gamefocal.rivenworld.game.tasks.HiveTask;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.WorldDirection;
import com.gamefocal.rivenworld.service.TaskService;

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

    public static HiveTask asyncFindPath(WorldCell start, WorldCell goal, AiPathResult promise, AiPathValidator validator) {
        return asyncFindPath(start, goal, promise, validator, new ArrayList<>(), 0);
    }

    public static HiveTask asyncFindPath(WorldCell start, WorldCell goal, AiPathResult promise, AiPathValidator validator, ArrayList<WorldCell> searchArea, float distToGoal) {
        return TaskService.sync(() -> {
            List<WorldCell> path = findPath(start, goal, validator, searchArea, distToGoal);
            promise.onPath(path);
        });
//        new Thread(() -> {
//            List<WorldCell> path = findPath(start, goal, validator, searchArea, distToGoal);
//            promise.onPath(path);
//        }).start();
    }

    public static PriorityQueue<WorldCell> getDistancePriorityQueue(Location goal) {
        return new PriorityQueue<WorldCell>((o1, o2) -> {
            double h1 = o1.getCenterInGameSpace(true).dist(goal);
            double h2 = o2.getCenterInGameSpace(true).dist(goal);

            if (h1 > h2) {
                return +1;
            } else if (h1 < h2) {
                return -1;
            } else {
                return 0;
            }
        });
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

    public static WorldCell findClosestTraversableCell(WorldCell from, int maxDistance) {
        ArrayList<WorldCell> inclosed = findEnclosedCells(from, maxDistance);

        HashMap<WorldCell, Integer> distances = new HashMap<>();
        Queue<WorldCell> queue = new LinkedList<>();

        queue.add(from);
        distances.put(from, 0);

        while (!queue.isEmpty()) {
            WorldCell current = queue.poll();
            int currentDistance = distances.get(current);

            if (!inclosed.contains(current) && current.isCanTraverse()) {
                return current;
            }

            if (currentDistance < maxDistance) {
                for (WorldCell neighbor : current.getNeighbors(true)) {
                    if (!distances.containsKey(neighbor)) {
                        queue.add(neighbor);
                        distances.put(neighbor, currentDistance + 1);
                    }
                }
            }
        }

        return null;
    }

    public static LinkedList<WorldCell> getEdgesInArea(WorldCell start, int maxDistance) {
        // Scan each direction until we find a wall.

        LinkedList<WorldCell> edge = new LinkedList<>();
        LinkedList<WorldCell> visited = new LinkedList<>();


        WorldCell wall = null;
        WorldCell cursor = start;
        boolean foundWallCircut = false;
        while (cursor.distanceTo(start) <= (maxDistance * 100)) {
            if (wall == null) {
                // North
                cursor = cursor.getNorth();
                if (!cursor.isCanTraverse()) {
                    // Return a wall
                    wall = cursor;
                }
            }

            if (wall != null) {
                // Scan the wall here

                boolean foundOtherWall = false;

                edge.add(wall);
                for (WorldCell c : wall.getNeighbors(true)) {
                    if (!visited.contains(c)) {
                        visited.add(c);
                        if (!c.isCanTraverse()) {
                            wall = c;
                            foundOtherWall = true;
                            break;
                        }
                    }
                }

                if (!foundOtherWall) {

                    boolean foundRescan = false;

                    // Could not find another wall attached... re-scan to find a starting point
                    for (WorldCell c : edge) {
                        for (WorldCell n : c.getNeighbors(true)) {
                            if (!visited.contains(n)) {
                                visited.add(n);
                                if (!n.isCanTraverse()) {
                                    wall = c;
                                    foundRescan = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (!foundRescan) {
                        break;
                    }
                }
            }
        }

        return edge;
    }

    public static ArrayList<WorldCell> findEnclosedCells(WorldCell start, int maxDistance) {
        // Open set for cells to explore
        Queue<WorldCell> openSet = new LinkedList<>();
        openSet.add(start);

        // Set for enclosed cells
        ArrayList<WorldCell> enclosedCells = new ArrayList<>();

        // Set for visited cells to avoid visiting the same cell multiple times
        Set<WorldCell> visitedCells = new HashSet<>();
        visitedCells.add(start);

        while (!openSet.isEmpty()) {
            WorldCell current = openSet.poll();

            // Check all neighbors
            Collection<WorldCell> neighbors = current.getNeighbors(true);

            boolean isEnclosed = true;
            for (WorldCell neighbor : neighbors) {
                // If a neighbor is traversable, then the current cell is not enclosed
                if (neighbor.isCanTraverse()) {
                    isEnclosed = false;
                }

                // If a neighbor has not been visited and is within the maxDistance, add it to the openSet
                if (!visitedCells.contains(neighbor) && current.distanceTo(neighbor) <= maxDistance) {
                    openSet.add(neighbor);
                    visitedCells.add(neighbor);
                }
            }

            // If all neighbors are non-traversable, then the current cell is enclosed
            if (isEnclosed) {
                enclosedCells.add(current);
            }
        }

        return enclosedCells;
    }

    public static boolean isAreaEnclosed(WorldCell start, int maxDistance) {
        ArrayList<WorldCell> enclosedCells = findEnclosedCells(start, maxDistance);

        // If there are no enclosed cells, the area is not enclosed
        if (enclosedCells.isEmpty()) {
            return false;
        }

        // Traverse the list of enclosed cells
        for (WorldCell cell : enclosedCells) {
            // Get the neighbors of the current cell
            Collection<WorldCell> neighbors = cell.getNeighbors(true);

            // Check each neighbor
            for (WorldCell neighbor : neighbors) {
                // If any neighbor is traversable, the area is not enclosed
                if (neighbor.isCanTraverse()) {
                    return false;
                }
            }
        }

        // If all neighbors of all enclosed cells are non-traversable, the area is enclosed
        return true;
    }

    public static ArrayList<WorldCell> findEdgeCells(ArrayList<WorldCell> enclosedCells) {
        ArrayList<WorldCell> edgeCells = new ArrayList<>();

        for (WorldCell cell : enclosedCells) {
            for (WorldCell neighbor : cell.getNeighbors(true)) {
                if (!enclosedCells.contains(neighbor)) {
                    edgeCells.add(cell);
                    break;  // Once we've found one outside neighbor, we know this is an edge cell
                }
            }
        }

        return edgeCells;
    }

    private static void recursiveFind(WorldCell current, ArrayList<WorldCell> enclosedCells, HashSet<WorldCell> visited, int maxDistance, int currentDistance) {
        // If we've already visited this cell or we've exceeded the maximum distance, stop here
        if (visited.contains(current) || currentDistance > maxDistance) {
            return;
        }
        visited.add(current);

        boolean isEnclosed = true;
        for (WorldCell neighbor : current.getNeighbors(true)) {
            if (neighbor.isCanTraverse()) {
                isEnclosed = false;
                recursiveFind(neighbor, enclosedCells, visited, maxDistance, currentDistance + 1);
            }
        }

        if (isEnclosed) {
            enclosedCells.add(current);
        }
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
