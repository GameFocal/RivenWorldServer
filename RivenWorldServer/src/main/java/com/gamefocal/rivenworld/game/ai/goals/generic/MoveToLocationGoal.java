package com.gamefocal.rivenworld.game.ai.goals.generic;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.vote.PeerVoteRequest;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ProjectedLocation;
import com.gamefocal.rivenworld.service.PeerVoteService;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class MoveToLocationGoal extends AiGoal {

    protected Location goal;
    protected Long startedAt = 0L;
    protected float distToGoal = 0.0f;
    protected ConcurrentLinkedQueue<Location> points = new ConcurrentLinkedQueue<>();
    protected Location waypoint = null;
    protected Location lastEntityLocation = null;
    protected Long lastCheckup = 0L;
    protected Location pointerLocation;
    protected boolean move = false;

    @Override
    public void onStart(LivingEntity livingEntity) {
        this.distToGoal = livingEntity.location.dist(this.goal);

//        System.out.println("Goal: " + this.goal.toString() + " dst " + this.distToGoal);

        DedicatedServer.get(PeerVoteService.class).createVote(new PeerVoteRequest("path", new String[]{livingEntity.uuid.toString(), goal.toString()}, request1 -> {

            if (request1.isTimedOut()) {
                complete(livingEntity);
                return;
            }

            String mostCommon = request1.mostCommon();

            if (!mostCommon.equalsIgnoreCase("~")) {

                System.out.println("Common: " + mostCommon);

                String[] locs = mostCommon.split("\\|");
                for (String l : locs) {
                    Location ll = Location.fromString(l);
                    if (ll != null) {
                        points.add(ll);
                    }
                }

                System.out.println("# LOCs Strings: " + locs.length);
                System.out.println("Got " + this.points.size() + " waypoints...");

                if (this.points.size() == 0) {
                    complete(livingEntity);
                    return;
                }

                move = true;
            } else {
//                System.out.println("Complete.");
                complete(livingEntity);
            }

        }), 3, livingEntity.location);
    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        // Find the points.
        if (this.waypoint == null) {
            if (this.points.size() > 0 && this.move) {
                this.waypoint = this.points.poll();
                this.lastEntityLocation = livingEntity.location.cpy();
                this.startedAt = System.currentTimeMillis();
                System.out.println("Assign waypoint " + this.waypoint.toString());
            } else if (this.startedAt > 0) {
                this.complete(livingEntity);
            }
        }

        if (this.waypoint != null) {

            final ProjectedLocation projectedLocation = new ProjectedLocation(this.lastEntityLocation, this.waypoint, System.currentTimeMillis() - this.startedAt, livingEntity.speed);

            livingEntity.location = projectedLocation.getPosition();

//            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastCheckup) >= 10) {
//                livingEntity.location = this.pointerLocation;
//                this.lastCheckup = System.currentTimeMillis();
//            }

            if (projectedLocation.getPercent() >= 1) {
                this.waypoint = null;
                System.out.println("Finish with travel");
            }
        }
    }

    public Location getGoal() {
        return goal;
    }

    @Override
    public void onEnd(LivingEntity livingEntity) {
//        System.out.println("ON END");
    }

    @Override
    public void takeOwnership(LivingEntity livingEntity, HiveNetConnection connection) {
        connection.sendOwnershipRequest(livingEntity, this.goal, "move", new JsonObject());
    }

    @Override
    public void releaseOwnership(LivingEntity livingEntity) {

    }

    @Override
    public void onOwnershipCmd(LivingEntity livingEntity, HiveNetConnection connection, String cmd, JsonObject data) {
        // TODO: Set the path
        if (cmd.equalsIgnoreCase("p")) {
            // Path
            for (String s : data.get("p").getAsString().split("\\&")) {
                this.points.add(Location.fromString(s));
            }
        }

        // TODO: Validate the path
    }
}
