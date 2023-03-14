package com.gamefocal.rivenworld.game.ai.goals;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.ray.RayRequestCallback;
import com.gamefocal.rivenworld.game.ray.UnrealTerrainRayRequest;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;
import com.gamefocal.rivenworld.service.AiService;
import com.gamefocal.rivenworld.service.PlayerService;
import com.gamefocal.rivenworld.service.RayService;
import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.Map;

public class RandomLocationGoal extends AiGoal<LinkedList<Location>> {

    private Location goal;
    private Long startedAt = 0L;
    private float distToGoal = 0.0f;
    private LinkedList<Location> points = new LinkedList<>();
    private Location waypoint = null;
    private Location lastEntityLocation = null;

    @Override
    public void onStart(LivingEntity livingEntity) {
        Location random = LocationUtil.getRandomLocationInRadius(1000, livingEntity.location);
        this.goal = random;
        this.distToGoal = livingEntity.location.dist(random);

        DedicatedServer.get(RayService.class).makeRequest(goal, 3, new RayRequestCallback() {
            @Override
            public void run(UnrealTerrainRayRequest request) {
                goal = request.getReturnedLocation();
                DedicatedServer.get(AiService.class).requestPathFindingFromClientPool(livingEntity, 3, goal);
            }
        });
    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        if (this.points.size() > 0) {
            // Find the points.
            if (this.waypoint == null) {
                this.waypoint = this.points.poll();
                this.lastEntityLocation = livingEntity.location.cpy();
                this.startedAt = System.currentTimeMillis();
            }
        }

        if (this.waypoint != null) {
            // Update the location
            Location location = LocationUtil.projectLocationFromStartWithSpeed(
                    this.lastEntityLocation,
                    this.waypoint,
                    System.currentTimeMillis() - this.startedAt,
                    livingEntity.speed
            );

//            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//                connection.drawDebugSphere(location, 5, 1);
//            }

//            livingEntity.location = location;

            if (livingEntity.location.toVector().dst(this.waypoint.toVector()) <= 5) {
                this.waypoint = null;
            }
        }

    }

    @Override
    public void onEnd(LivingEntity livingEntity) {

    }

    @Override
    public void getState(Map<String, Object> meta) {
        if (this.waypoint != null) {
            meta.put("goal", this.waypoint.toString());
        }
    }

    @Override
    public void onNetSync(LinkedList<Location> locations) {
        locations.poll();
        this.points = locations;
    }
}
