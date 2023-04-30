package com.gamefocal.rivenworld.game.ai.goals.generic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.ai.path.AStarPathfinding;
import com.gamefocal.rivenworld.game.ai.path.LocationPathFinder;
import com.gamefocal.rivenworld.game.ai.path.SimplePathfinder;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;
import com.gamefocal.rivenworld.game.util.VectorUtil;
import com.gamefocal.rivenworld.service.PlayerService;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MoveToLocationGoal extends AiGoal {

    protected Location goal = null;
    private Vector3 subGoal = null;
    private Vector3 subGoalStart = null;
    private LinkedList<Vector3> waypoints = new LinkedList<>();
    private long subGoalStartAt = 0L;

    public MoveToLocationGoal(Location goal) {
        this.goal = goal;
    }

    public MoveToLocationGoal() {
    }

    @Override
    public void onStart(LivingEntity livingEntity) {

//        SimplePathfinder pathfinder = new SimplePathfinder(DedicatedServer.instance.getWorld().getCollisionManager().getOctree(), 100, 100, 500);

//        List<Vector3> vector3s = pathfinder.findPath(livingEntity.location.toVector(),this.goal.toVector());

//        LocationPathFinder pathFinder = new LocationPathFinder(DedicatedServer.instance.getWorld().getGrid());

//        LinkedList<WorldCell> cells = pathFinder.findPathTo(livingEntity.location, this.goal);

        WorldCell startingCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(livingEntity.location.cpy());
        WorldCell goalCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(this.goal.cpy());

        List<WorldCell> cells = AStarPathfinding.findPath(startingCell, goalCell);

        if(cells == null) {
            System.err.println("Invalid Path...");
            return;
        }

        int i = 0;
        for (WorldCell cell : cells) {
            Vector3 centerVector = cell.getCenterInGameSpace(true).toVector();
            if(centerVector.z > 0) {
                this.waypoints.add(centerVector);
            }
        }
    }

    @Override
    public void onComplete(LivingEntity livingEntity) {

    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        if (this.subGoal == null && this.waypoints.size() > 0) {
            // Has a new goal
            this.subGoal = this.waypoints.poll().cpy();
            this.subGoalStart = livingEntity.location.toVector();
            this.subGoalStartAt = System.currentTimeMillis();
        }

        /*
         * Move the entity
         * */
        if (this.subGoal != null) {
            // Calc total travel time using the speed of the entity

//            float timeToTravel = ((Math.abs(this.subGoalStart.dst(this.subGoal)) / livingEntity.speed)*1000);
//            float timeSpent = System.currentTimeMillis() - this.subGoalStartAt;
//
//            float percent = timeSpent / timeToTravel;

            if (livingEntity.location.toVector().epsilonEquals(this.subGoal,10)) {
                this.subGoal = null;
                this.subGoalStart = null;
                this.subGoalStartAt = 0L;
                this.onTick(livingEntity);
                return;
            }

//            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//                connection.drawDebugLine(Color.GREEN, livingEntity.location, Location.fromVector(this.subGoal), 2);
//            }

            Vector3 newLoc = livingEntity.location.toVector();
            Vector3 dir = this.subGoal.cpy().sub(livingEntity.location.toVector()).nor();
            newLoc.add(dir);

//            Vector3 newLoc = this.subGoalStart.interpolate(this.subGoal, percent, Interpolation.linear);

            livingEntity.location = Location.fromVector(newLoc);
//            livingEntity.location.lookAt(this.subGoal);

            double deg = VectorUtil.getDegrees(livingEntity.location.toVector(),this.subGoal);

            livingEntity.location.setRotation(0,0, (float) deg);

//            for (Vector3 v : this.waypoints) {
//                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//                    connection.drawDebugBox(Color.ORANGE,Location.fromVector(v),new Location(50,50,50),2);
//                }
//            }

            // TODO: Trigger animations here

        }
    }
}
