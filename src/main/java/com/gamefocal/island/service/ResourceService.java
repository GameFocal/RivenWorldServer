package com.gamefocal.island.service;

import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.models.GameResourceNode;
import com.google.auto.service.AutoService;
import com.j256.ormlite.stmt.QueryBuilder;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Singleton
@AutoService(HiveService.class)
public class ResourceService implements HiveService<ResourceService> {

    public ArrayList<Location> pendingLocations = new ArrayList<>();

    public LinkedList<GameResourceNode> getNodesNearby(Location location, float radius, Location ground) {
        LinkedList<GameResourceNode> nodes = new LinkedList<>();

        Sphere search = new Sphere(location.toVector(), radius);

        try {
            for (GameResourceNode n : DataService.resourceNodes.queryForAll()) {
                Sphere check = new Sphere(n.realLocation.setZ(ground.getZ()).toVector(), 100);
                if (search.overlaps(check)) {
                    // Is within the check region
                    nodes.add(n);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return nodes;
    }

    public void spawnNearbyNodes(HiveNetConnection connection, float radius) {
        Sphere search = new Sphere(connection.getPlayer().location.cpy().setZ(0).toVector(), radius);

        Long now = System.currentTimeMillis();

        QueryBuilder<GameResourceNode, String> q = DataService.resourceNodes.queryBuilder();
        try {
            q.where().le("nextSpawn", now).and().eq("spawned", false);

            List<GameResourceNode> nodes = q.query();

            for (GameResourceNode n : nodes) {
                if (n != null) {
                    Sphere s = new Sphere(n.location.cpy().setZ(0).toVector(), 100);

                    if (search.overlaps(s)) {

                        if (n.location != null) {
                            if (!this.pendingLocations.contains(n.location)) {
                                this.pendingLocations.add(n.location);

                                DedicatedServer.get(RayService.class).makeRequest(n.location, 3, request -> {
                                    // Spawn the node here :)
                                    n.realLocation = n.location.cpy().setZ(request.getReturnedLocation().getZ());

                                    n.spawnEntity.location = n.realLocation;
                                    n.spawnEntity.setMeta("rn", n.uuid.toString());

                                    // Spawn the entity
                                    GameEntityModel entityModel = DedicatedServer.instance.getWorld().spawn(n.spawnEntity, n.realLocation);

                                    n.spawned = true;
                                    n.attachedEntity = entityModel.uuid;
                                    try {
                                        DataService.resourceNodes.update(n);
                                        this.pendingLocations.remove(n.location);
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }
                                });
                            }
                        }
                    }
                }

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void init() {

    }
}
