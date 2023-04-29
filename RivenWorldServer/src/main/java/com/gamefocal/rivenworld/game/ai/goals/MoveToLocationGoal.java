package com.gamefocal.rivenworld.game.ai.goals;

import com.badlogic.gdx.graphics.Color;
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

        System.out.println("Found path of " + cells.size());


        Location first = cells.get(0).getCenterInGameSpace(true,200);

        for (WorldCell c : cells) {

            Location l2 = c.getCenterInGameSpace(true,200);

            System.out.println(l2.toString());

            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                connection.drawDebugLine(Color.GREEN,first,l2,2);
            }

            first = l2;
        }

//        ArrayList<WorldCell> p =
//
//        System.out.println(vector3s.size());

    }

    @Override
    public void onComplete(LivingEntity livingEntity) {

    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        if (this.subGoal == null && this.waypoints.size() > 0) {
            // Has a new goal
            this.subGoal = this.waypoints.poll();
            this.subGoalStart = livingEntity.location.toVector();
            this.subGoalStartAt = System.currentTimeMillis();
        }

        /*
         * Move the entity
         * */
        if (this.subGoal != null) {
            // Calc total travel time using the speed of the entity
            float timeToTravel = (this.subGoalStart.dst(this.subGoal) / livingEntity.speed) * 1000;
            float timeSpent = System.currentTimeMillis() - this.subGoalStartAt;

            float percent = timeSpent / timeToTravel;

            if (percent > 1) {
                // Is a complete travel
                this.subGoal = null;
                this.subGoalStart = null;
                return;
            }

            Vector3 newLoc = this.subGoalStart.lerp(this.subGoal, percent);
            livingEntity.location = Location.fromVector(newLoc);
            livingEntity.location.lookAt(this.subGoal);

//            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//                connection.drawDebugLine(Color.GREEN, livingEntity.location, Location.fromVector(this.subGoal), 2);
//            }

            // TODO: Trigger animations here

        }
    }
}
