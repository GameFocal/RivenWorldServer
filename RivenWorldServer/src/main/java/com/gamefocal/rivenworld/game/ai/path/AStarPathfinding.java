package com.gamefocal.rivenworld.game.ai.path;

import java.util.*;

public class AStarPathfinding {
    public static List<WorldCell> findPath(WorldCell start, WorldCell goal) {
        Set<WorldCell> openSet = new HashSet<>();
        Set<WorldCell> closedSet = new HashSet<>();
        Map<WorldCell, WorldCell> cameFrom = new HashMap<>();

        Map<WorldCell, Float> gScore = new HashMap<>();
        gScore.put(start, 0f);

        Map<WorldCell, Float> fScore = new HashMap<>();
        fScore.put(start, heuristicCostEstimate(start, goal));

        openSet.add(start);

        while (!openSet.isEmpty()) {
            WorldCell current = getCellWithLowestFScore(openSet, fScore);
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            openSet.remove(current);
            closedSet.add(current);

            for (WorldCell neighbor : current.getNeighbors(true)) {
                if (closedSet.contains(neighbor) || !neighbor.isCanTraverse()) {
                    continue;
                }

                float slope = Math.abs(neighbor.getHeight() - current.getHeight());

                if (slope > 200) {
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
