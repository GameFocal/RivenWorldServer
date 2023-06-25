package com.gamefocal.rivenworld.game.ai.goals.generic;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ai.path.AStarPathfinding;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.ArrayList;
import java.util.List;

public abstract class FastMoveToLocation extends MoveToLocationGoal {
    protected Location targetTrackLocation = null;
    protected Location actualLocation = null;
    protected int maxDistance = 20;

    public FastMoveToLocation(Location location) {
        this.goal = location;
        this.targetTrackLocation = location;
    }

    public abstract void updateActualLocation(LivingEntity livingEntity);

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    @Override
    public void onTick(LivingEntity livingEntity) {

        this.updateActualLocation(livingEntity);

        WorldCell currentCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(livingEntity.location);
        WorldCell goalCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(actualLocation);
//        WorldCell forwardCell = currentCell.getNeighborFromFwdVector(livingEntity.getVelocity());

//        boolean useQuickVector = false;
//        if (forwardCell.canTravelFromCell(currentCell, null)) {
//            // Can travel using quick method...
//            useQuickVector = true;
//        }

//        Vector3 dir = actualLocation.toVector().cpy().sub(livingEntity.location.toVector()).nor();
//
//        Ray ray = new Ray(livingEntity.location.toVector().add(0, 0, 100), dir);
//
//        for (GameEntity n : DedicatedServer.instance.getWorld().getCollisionManager().getNearbyEntities(livingEntity)) {
//            if (n.location.dist(livingEntity.location) <= (livingEntity.location.dist(actualLocation))) {
//                if (Intersector.intersectRayBounds(ray, n.getBoundingBox(), new Vector3())) {
//                    useQuickVector = true;
//                    break;
//                }
//            }
//        }

        /*
         * Reroute using local if needed
         * */
//        if (!useQuickVector) {
            if (targetTrackLocation == null || !targetTrackLocation.toVector().epsilonEquals(actualLocation.toVector(), 100)) {
                // Has moved...

                if (goalCell != null) {
                    ArrayList<WorldCell> around = currentCell.getRadiusCells(this.maxDistance);

                    this.subGoal = null;
                    this.subGoalStart = null;
                    this.subGoalStartAt = 0L;
                    this.waypoints.clear();
                    hasPath = false;
                    this.goal = goalCell.getCenterInGameSpace(true);

                    List<WorldCell> cells = AStarPathfinding.findPath(currentCell, goalCell, null, around, 0);

                    if (cells == null) {
                        livingEntity.resetSpeed();
                        livingEntity.resetVelocity();
                        livingEntity.isAggro = false;
                        this.complete(livingEntity);
                        return;
                    }

                    hasPath = true;

                    for (WorldCell cell : cells) {
                        Vector3 centerVector = cell.getCenterInGameSpace(true).toVector();
                        if (centerVector.z > 0) {
                            this.waypoints.add(centerVector);
                        }
                    }
                }

                targetTrackLocation = actualLocation.cpy();
            }

            livingEntity.setLocationGoal(this.actualLocation.toVector());

            super.onTick(livingEntity);
//        } else {
//            /*
//             * Quick vector
//             * */
//            targetTrackLocation = actualLocation.cpy();
//
//            livingEntity.setVelocity(dir);
//            livingEntity.setLocationGoal(this.targetTrackLocation.toVector());
//
//            livingEntity.setVelocity(dir);
//        }
    }
}
