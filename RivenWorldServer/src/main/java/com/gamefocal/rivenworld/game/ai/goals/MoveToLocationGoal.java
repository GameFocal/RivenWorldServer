package com.gamefocal.rivenworld.game.ai.goals;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.PlayerService;

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

        System.out.println("Starting...");

//        AStar star = new AStar(DedicatedServer.instance.getWorld().getCollisionManager());
//        List<Vector3> locs = star.findPath(livingEntity.location.toVector(), this.goal.toVector());
//        this.waypoints.addAll(locs);

//        AStarPathfinder aStarPathfinder = new AStarPathfinder(DedicatedServer.instance.getWorld().getCollisionManager(), DedicatedServer.instance.getWorld().getRawHeightmap());
//        List<Vector3> locs = aStarPathfinder.findPath(livingEntity.location.toVector(), this.goal.toVector());

//        this.pathfinder = new AStarPathfinder(DedicatedServer.instance.getWorld().getCollisionManager().getOctree(), 201753, 201753, 201753);
//        List<Vector3> locs = this.pathfinder.findPath(livingEntity.location.toVector(), this.goal.toVector());

//        if (locs == null) {
//            this.complete(livingEntity);
//            return;
//        }
//
//        for (Vector3 l : locs) {
//
//            /*
//             * Generate using the world heights
//             * */
//
//            Location loc = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(Location.fromVector(l));
//            this.waypoints.add(loc.toVector());
//        }

        System.out.println("Assigned travel with " + this.waypoints.size() + " points...");
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

            System.out.println("Assign Goal: " + this.subGoal);
            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                connection.drawDebugLine(livingEntity.location.cpy(), Location.fromVector(this.subGoal), 2);
            }

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

            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                connection.drawDebugLine(livingEntity.location, Location.fromVector(this.subGoal), 2);
            }

            // TODO: Trigger animations here

        }
    }
}
