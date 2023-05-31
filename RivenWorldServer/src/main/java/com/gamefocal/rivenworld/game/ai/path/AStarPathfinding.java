package com.gamefocal.rivenworld.game.ai.path;

import com.gamefocal.rivenworld.DedicatedServer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class AStarPathfinding {

    public static ConcurrentHashMap<UUID, Integer> pathFindingAttempts = new ConcurrentHashMap<>();

    public static void asyncFindPath(WorldCell start, WorldCell goal, AiPathResult promise) {
        new Thread(() -> {
            List<WorldCell> path = findPath(start, goal);
            promise.onPath(path);
        }).start();
    }

    public static List<WorldCell> findPath(WorldCell start, WorldCell goal) {
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

                float myHeight = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(current.getCenterInGameSpace(false));
                float nHeight = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(neighbor.getCenterInGameSpace(false));

                float slope = Math.abs(nHeight - myHeight);

                // Prevent steep slope
                if (slope > 50) {
                    continue;
                }

                // Prevent below sea level
                if (neighbor.getCenterInGameSpace(true).getZ() <= 3000) {
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
}
