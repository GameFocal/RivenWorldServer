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
    protected Location pointer = new Location(0, 0, 0);
    protected LivingEntity livingEntity;

    @Override
    public void onStart(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    public abstract void findGoal();

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
            this.pointer = projectedLocation.getPosition();

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
        System.out.println("JOB OWNERSHIP CHANGE");
        connection.sendOwnershipRequest(livingEntity, this.goal, "move", new JsonObject());
    }

    @Override
    public void releaseOwnership(LivingEntity livingEntity) {
        System.out.println("JOB OWNERSHIP RELEASE");
    }

    @Override
    public void onOwnershipCmd(LivingEntity livingEntity, HiveNetConnection connection, String cmd, JsonObject data) {

        // TODO: Set the path
        if (cmd.equalsIgnoreCase("p")) {
            // Path

            System.out.println("PATH: " + data.get("p").getAsString());

            if(data.get("p").getAsString().isEmpty()) {
                this.findGoal();
                connection.sendOwnershipRequest(livingEntity, this.goal, "move", new JsonObject());
                return;
            }

            for (String s : data.get("p").getAsString().split("&")) {
                this.points.add(Location.fromString(s));
            }

            this.move = true;
        }

        // TODO: Validate the path
    }

    @Override
    public boolean validatePeerUpdate(LivingEntity livingEntity, HiveNetConnection connection, Location location) {
        // Check to see if the AI is near the set location path
        if (location.dist(this.pointer) <= 200) {
            return true;
        } else {
            location = this.pointer.cpy();
        }

        return true;
    }
}
