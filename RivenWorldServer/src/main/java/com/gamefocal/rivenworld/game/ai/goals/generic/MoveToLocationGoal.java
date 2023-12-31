package com.gamefocal.rivenworld.game.ai.goals.generic;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.ai.path.AStarPathfinding;
import com.gamefocal.rivenworld.game.ai.path.AiPathValidator;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.ArrayList;
import java.util.LinkedList;

public class MoveToLocationGoal extends AiGoal {

    protected Location goal = null;
    protected boolean hasPath = false;
    protected boolean isSearching = false;
    protected Vector3 subGoal = null;
    protected Vector3 subGoalStart = null;
    protected LinkedList<Vector3> waypoints = new LinkedList<>();
    protected long subGoalStartAt = 0L;
    protected AiPathValidator pathValidator = null;

    public MoveToLocationGoal(Location goal) {
        this.goal = goal;
    }

    public MoveToLocationGoal(AiPathValidator pathValidator) {
        this.pathValidator = pathValidator;
    }

    public MoveToLocationGoal(Location goal, AiPathValidator pathValidator) {
        this.goal = goal;
        this.pathValidator = pathValidator;
    }

    public MoveToLocationGoal() {
    }

    public void reroutePath(LivingEntity livingEntity, Location location) {
        reroutePath(livingEntity, location, new ArrayList<>());
    }

    public boolean onGoalReached(LivingEntity livingEntity) {
        return true;
    }

    public void reroutePath(LivingEntity livingEntity, Location location, ArrayList<WorldCell> searchCells) {
        livingEntity.setLocationGoal(location.toVector());
        if (!isSearching) {
            isSearching = true;
            this.subGoal = null;
            this.subGoalStart = null;
            this.subGoalStartAt = 0L;
            this.waypoints.clear();
            hasPath = false;
            this.goal = location;

            WorldCell startingCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(livingEntity.location.cpy());
            WorldCell goalCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(this.goal.cpy());

            AStarPathfinding.asyncFindPath(startingCell, goalCell, cells -> {
                isSearching = false;
                if (cells == null) {
                    int attempts = 0;
                    if (AStarPathfinding.pathFindingAttempts.containsKey(livingEntity.uuid)) {
                        attempts = AStarPathfinding.pathFindingAttempts.get(livingEntity.uuid);
                    }

                    if (attempts > 3) {
                        DedicatedServer.instance.getWorld().despawn(livingEntity.uuid);
                        return;
                    }

                    AStarPathfinding.pathFindingAttempts.put(livingEntity.uuid, ++attempts);

//                    System.err.println("Invalid Path...");
                    complete(livingEntity);
                    return;
                }

                AStarPathfinding.pathFindingAttempts.remove(livingEntity.uuid);

                for (WorldCell cell : cells) {
                    Vector3 centerVector = cell.getCenterInGameSpace(true).toVector();
                    if (centerVector.z > 0) {
                        waypoints.add(centerVector);
                    }
                }

                hasPath = true;
            }, pathValidator, searchCells, 0);
        }
    }

    public AiPathValidator getPathValidator() {
        return pathValidator;
    }

    public void setPathValidator(AiPathValidator pathValidator) {
        this.pathValidator = pathValidator;
    }

    @Override
    public void onStart(LivingEntity livingEntity) {
        this.reroutePath(livingEntity, this.goal);
    }

    @Override
    public void onComplete(LivingEntity livingEntity) {
        livingEntity.isMoving = false;
        livingEntity.resetVelocity();
    }

    public Vector3 getSubGoal() {
        return subGoal;
    }

    public LinkedList<Vector3> getWaypoints() {
        return waypoints;
    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        if (hasPath) {
            if (this.subGoal == null && this.waypoints.size() > 0) {
                // Has a new goal
                this.subGoal = this.waypoints.poll().cpy();
                this.subGoalStart = livingEntity.location.toVector();
                this.subGoalStartAt = System.currentTimeMillis();
            } else if (this.subGoal == null) {
                // Is done
                if (this.onGoalReached(livingEntity)) {
                    livingEntity.isMoving = false;
                    livingEntity.resetVelocity();
                    this.complete(livingEntity);
                }
                return;
            }

            /*
             * Move the entity
             * */
            if (this.subGoal != null) {

                livingEntity.isMoving = true;

                // Calc total travel time using the speed of the entity

//            float timeToTravel = ((Math.abs(this.subGoalStart.dst(this.subGoal)) / livingEntity.speed)*1000);
//            float timeSpent = System.currentTimeMillis() - this.subGoalStartAt;
//
//            float percent = timeSpent / timeToTravel;


                Vector3 subGoalCpy = this.subGoal.cpy();
                subGoalCpy.z = 0;

                if (livingEntity.location.cpy().setZ(0).toVector().epsilonEquals(subGoalCpy, 100)) {
                    this.subGoal = null;
                    this.subGoalStart = null;
                    this.subGoalStartAt = 0L;
                    return;
                }

//                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//                    connection.drawDebugLine(Color.GREEN, livingEntity.location, Location.fromVector(this.subGoal), 2);
//                    for (Vector3 w : this.waypoints) {
//                        connection.drawDebugBox(Color.YELLOW, Location.fromVector(w), new Location(50, 50, 50), 1);
//                    }
//                }

//                Vector3 newLoc = livingEntity.location.toVector();
                Vector3 dir = this.subGoal.cpy().sub(livingEntity.location.toVector()).nor();
//                newLoc.mulAdd(dir, (livingEntity.speed * 2));

                livingEntity.setVelocity(dir);
                livingEntity.setLocationGoal(this.subGoal.cpy());

//            Vector3 newLoc = this.subGoalStart.interpolate(this.subGoal, percent, Interpolation.linear);

//                livingEntity.location = Location.fromVector(newLoc);
//            livingEntity.location.lookAt(this.subGoal);

//                double deg = VectorUtil.getDegrees(livingEntity.location.toVector(), this.subGoal);

//                livingEntity.location.setRotation(0, 0, (float) deg);

//            for (Vector3 v : this.waypoints) {
//                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//                    connection.drawDebugBox(Color.ORANGE,Location.fromVector(v),new Location(50,50,50),2);
//                }
//            }

                // TODO: Trigger animations here
            }
        }
    }
}
