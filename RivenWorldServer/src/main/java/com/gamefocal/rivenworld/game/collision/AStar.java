package com.gamefocal.rivenworld.game.collision;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.*;

public class AStar {
    private CollisionManager collisionManager;

    public AStar(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    public List<Vector3> findPath(Vector3 start, Vector3 goal) {
        Node startNode = new Node(start);
        Node goalNode = new Node(goal);

        PriorityQueue<Node> openList = new PriorityQueue<>();
        Set<Node> closedList = new HashSet<>();

        startNode.gScore = 0;
        startNode.fScore = startNode.getHeuristicDistance(goalNode);

        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            if (current.equals(goalNode)) {
                return reconstructPath(current);
            }

            closedList.add(current);

            for (Node neighbor : getNeighbors(current, goalNode)) {
                if (closedList.contains(neighbor)) {
                    continue;
                }

                float tentativeGScore = current.gScore + current.getDistance(neighbor);
                if (!openList.contains(neighbor) || tentativeGScore < neighbor.gScore) {
                    neighbor.parent = current;
                    neighbor.gScore = tentativeGScore;
                    neighbor.fScore = neighbor.gScore + neighbor.getHeuristicDistance(goalNode);
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Node> getNeighbors(Node node, Node goalNode) {
        List<Node> neighbors = new ArrayList<>();
        for (GameEntity entity : collisionManager.getNearbyEntities(Location.fromVector(node.position))) {
            if (entity.getBoundingBox() == null) {
                continue;
            }
            Vector3 direction = entity.location.toVector().cpy().sub(node.position);
            float distance = direction.len();
            if (distance < node.getRadius() + (entity.getBoundingBox().getWidth() / 2)) {
                continue;
            }
            direction.scl(1.0f / distance);
            float stepSize = Math.min(distance - node.getRadius() - (entity.getBoundingBox().getWidth() / 2), node.getStepSize());
            Vector3 neighborPosition = node.position.cpy().add(direction.scl(stepSize));
            Node neighbor = new Node(neighborPosition);
            if (collisionManager.isColliding(entity, neighbor.getBoundingBox())) {
                continue;
            }
            neighbor.stepSize = stepSize;
            neighbors.add(neighbor);
        }
        neighbors.add(new Node(node.position.cpy().add(node.getStepSize(), 0, 0)));
        neighbors.add(new Node(node.position.cpy().add(-node.getStepSize(), 0, 0)));
        neighbors.add(new Node(node.position.cpy().add(0, node.getStepSize(), 0)));
        neighbors.add(new Node(node.position.cpy().add(0, -node.getStepSize(), 0)));
        neighbors.add(new Node(node.position.cpy().add(0, 0, node.getStepSize())));
        neighbors.add(new Node(node.position.cpy().add(0, 0, -node.getStepSize())));
        return neighbors;
    }

    private List<Vector3> reconstructPath(Node node) {
        List<Vector3> path = new ArrayList<>();
        while (node != null) {
            path.add(node.position);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public static class Node implements Comparable<Node> {
        private Vector3 position;
        private Node parent;
        private float gScore;
        private float fScore;
        private float stepSize = 1.0f;

        public Node(Vector3 position) {
            this.position = position;
        }

        public float getDistance(Node other) {
            return position.dst(other.position);
        }

        public float getHeuristicDistance(Node other) {
            return position.dst(other.position);
        }

        public float getRadius() {
            return 0.5f;
        }

        public BoundingBox getBoundingBox() {
            Vector3 min = new Vector3(position).sub(getRadius(), getRadius(), getRadius());
            Vector3 max = new Vector3(position).add(getRadius(), getRadius(), getRadius());
            return new BoundingBox(min, max);
        }

        public float getStepSize() {
            return stepSize;
        }

        public void setStepSize(float stepSize) {
            this.stepSize = stepSize;
        }

        @Override
        public int compareTo(Node other) {
            return Float.compare(fScore, other.fScore);
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Node) {
                Node otherNode = (Node) other;
                return position.equals(otherNode.position);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return position.hashCode();
        }
    }
}



