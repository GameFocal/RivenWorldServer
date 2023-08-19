package com.gamefocal.rivenworld.game.collision;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.GameEntity;

import java.util.ArrayList;
import java.util.List;

public class Octree {
    private static final int MAX_ENTITIES = 8;
    private static final int MAX_LEVELS = 5;

    private int level;
    private List<GameEntity> entities;
    private BoundingBox bounds;
    private Octree[] children;

    public Octree(int level, BoundingBox bounds) {
        this.level = level;
        this.bounds = bounds;
        entities = new ArrayList<>();
        children = new Octree[8];
    }

    public void clear() {
        entities.clear();

        for (int i = 0; i < children.length; i++) {
            if (children[i] != null) {
                children[i].clear();
                children[i] = null;
            }
        }
    }

    public boolean remove(GameEntity entity) {
        if (children[0] != null) {
            int index = getIndex(entity);
            if (index != -1) {
                boolean removed = children[index].remove(entity);
                if (removed) {
                    // Check if children nodes can be merged
                    int totalEntities = 0;
                    for (Octree child : children) {
                        totalEntities += child.entities.size();
                        if (child.children[0] != null || totalEntities > MAX_ENTITIES) {
                            return true;
                        }
                    }

                    // Merge children nodes into the current node
                    for (Octree child : children) {
                        entities.addAll(child.entities);
                        child.entities.clear();
                        child = null;
                    }
                }
                return removed;
            }
        }

        return entities.remove(entity);
    }

    private void split() {
        float width = bounds.getWidth() / 2;
        float height = bounds.getHeight() / 2;
        float depth = bounds.getDepth() / 2;
        Vector3 min = bounds.min;
        int nextLevel = level + 1;

        children[0] = new Octree(nextLevel, new BoundingBox(min, new Vector3(min.x + width, min.y + height, min.z + depth)));
        children[1] = new Octree(nextLevel, new BoundingBox(new Vector3(min.x + width, min.y, min.z), new Vector3(min.x + 2 * width, min.y + height, min.z + depth)));
        children[2] = new Octree(nextLevel, new BoundingBox(new Vector3(min.x, min.y + height, min.z), new Vector3(min.x + width, min.y + 2 * height, min.z + depth)));
        children[3] = new Octree(nextLevel, new BoundingBox(new Vector3(min.x + width, min.y + height, min.z), new Vector3(min.x + 2 * width, min.y + 2 * height, min.z + depth)));
        children[4] = new Octree(nextLevel, new BoundingBox(new Vector3(min.x, min.y, min.z + depth), new Vector3(min.x + width, min.y + height, min.z + 2 * depth)));
        children[5] = new Octree(nextLevel, new BoundingBox(new Vector3(min.x + width, min.y, min.z + depth), new Vector3(min.x + 2 * width, min.y + height, min.z + 2 * depth)));
        children[6] = new Octree(nextLevel, new BoundingBox(new Vector3(min.x, min.y + height, min.z + depth), new Vector3(min.x + width, min.y + 2 * height, min.z + 2 * depth)));
        children[7] = new Octree(nextLevel, new BoundingBox(new Vector3(min.x + width, min.y + height, min.z + depth), new Vector3(min.x + 2 * width, min.y + 2 * height, min.z + 2 * depth)));
    }

    private int getIndex(GameEntity entity) {
        BoundingBox entityBounds = entity.getBoundingBox();
        Vector3 entityMin = entityBounds.min;
        Vector3 entityMax = entityBounds.max;
        Vector3 center = bounds.getCenter(new Vector3());

        int index = -1;

        boolean top = entityMin.y > center.y;
        boolean bottom = entityMax.y <= center.y;
        boolean front = entityMin.z > center.z;
        boolean back = entityMax.z <= center.z;
        boolean left = entityMax.x <= center.x;
        boolean right = entityMin.x > center.x;

        if (left) {
            if (front) {
                if (top) {
                    index = 0;
                } else if (bottom) {
                    index = 4;
                }
            } else if (back) {
                if (top) {
                    index = 2;
                } else if (bottom) {
                    index = 6;
                }
            }
        } else if (right) {
            if (front) {
                if (top) {
                    index = 1;
                } else if (bottom) {
                    index = 5;
                }
            } else if (back) {
                if (top) {
                    index = 3;
                } else if (bottom) {
                    index = 7;
                }
            }
        }

        return index;
    }

    public void insert(GameEntity entity) {
        if (children[0] != null) {
            int index = getIndex(entity);

            if (index != -1) {
                children[index].insert(entity);
                return;
            }
        }

        entities.add(entity);

        if (entities.size() > MAX_ENTITIES && level < MAX_LEVELS) {
            if (children[0] == null) {
                split();
            }

            int i = 0;
            while (i < entities.size()) {
                GameEntity currentEntity = entities.get(i);
                int index = getIndex(currentEntity);
                if (index != -1) {
                    children[index].insert(currentEntity);
                    entities.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    public List<GameEntity> retrieve(List<GameEntity> returnEntities, GameEntity entity) {
        int index = getIndex(entity);
        if (index != -1 && children[0] != null) {
            children[index].retrieve(returnEntities, entity);
        }

        returnEntities.addAll(entities);
        return returnEntities;
    }
}
