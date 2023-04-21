package com.gamefocal.rivenworld.game.collision;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.TestBlock;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {
    private Octree octree;

    public CollisionManager(float worldSize) {
        BoundingBox gameWorldBounds = new BoundingBox(new Vector3(0, 0, 0), new Vector3(worldSize, worldSize, worldSize));
        octree = new Octree(0, gameWorldBounds);
    }

    public void addEntity(GameEntity entity) {
        octree.insert(entity);
    }

    public void removeEntity(GameEntity entity) {
        octree.remove(entity);
    }

    public void updateEntity(GameEntity entity, Vector3 oldPosition) {
        octree.remove(entity);
        octree.insert(entity);
    }

    public boolean checkCollision(GameEntity entityA, GameEntity entityB) {
        // Implement your collision detection logic here
        // For example, you could check if the bounding boxes of entityA and entityB intersect
        return entityA.getBoundingBox().intersects(entityB.getBoundingBox());
    }

    public List<GameEntity> getNearbyEntities(GameEntity entity) {
        List<GameEntity> nearbyEntities = new ArrayList<>();
        octree.retrieve(nearbyEntities, entity);
        return nearbyEntities;
    }

    public List<GameEntity> getNearbyEntities(Location loc) {
        TestBlock block = new TestBlock();
        block.location = loc;
        return this.getNearbyEntities(block);
    }

    public boolean isColliding(GameEntity entity, BoundingBox boundingBox) {
        if (entity.getBoundingBox() == null) {
            return false;
        }
        BoundingBox entityBox = entity.getBoundingBox();
        return entityBox.intersects(boundingBox);
    }
}
