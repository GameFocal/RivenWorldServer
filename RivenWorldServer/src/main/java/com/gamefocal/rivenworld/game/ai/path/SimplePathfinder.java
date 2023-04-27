package com.gamefocal.rivenworld.game.ai.path;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.collision.Octree;
import com.gamefocal.rivenworld.game.entites.blocks.TestBlock;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.ArrayList;
import java.util.List;

public class SimplePathfinder {
    private Octree octree;
    private float stepSize;
    private float raycastDistance;
    private float maxElevationChange;

    public SimplePathfinder(Octree octree, float stepSize, float raycastDistance, float maxElevationChange) {
        this.octree = octree;
        this.stepSize = stepSize;
        this.raycastDistance = raycastDistance;
        this.maxElevationChange = maxElevationChange;
    }

    public List<Vector3> findPath(Vector3 startPosition, Vector3 endPosition) {
        List<Vector3> path = new ArrayList<>();
        Vector3 currentPosition = new Vector3(startPosition);
        Vector3 direction = new Vector3();
        Ray ray = new Ray();

        while (currentPosition.dst(endPosition) > stepSize) {
            direction.set(endPosition).sub(currentPosition).nor();
            ray.set(currentPosition, direction);
            Vector3 nextPosition = new Vector3(currentPosition).add(direction.scl(raycastDistance));
            DedicatedServer.instance.getWorld().getRawHeightmap().getHeightVectorFromVector(nextPosition);

            TestBlock block = new TestBlock();
            block.location = Location.fromVector(currentPosition);

            boolean hit = false;
            List<GameEntity> nearbyEntities = octree.retrieve(new ArrayList<>(), block);

            for (GameEntity entity : nearbyEntities) {
                if (Intersector.intersectRayBoundsFast(ray, entity.getBoundingBox())) {
                    hit = true;
                    break;
                }
            }

            if (Math.abs(nextPosition.y - currentPosition.y) > maxElevationChange) {
                hit = true;
            }

            /*
             * Check for areas that are not traversable
             * */
            // TODO: Above

            if (!hit) {
                currentPosition.set(nextPosition);
                path.add(new Vector3(currentPosition));
            } else {
                // Handle path obstruction here, such as finding an alternative route
                break;
            }
        }

        return path;
    }
}
