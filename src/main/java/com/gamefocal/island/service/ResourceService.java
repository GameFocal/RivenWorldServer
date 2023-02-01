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
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Singleton
@AutoService(HiveService.class)
public class ResourceService implements HiveService<ResourceService> {

    public ConcurrentHashMap<UUID, Pair<Long, GameResourceNode>> pendingLocationRequests = new ConcurrentHashMap<>();

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

                Sphere s = new Sphere(n.location.cpy().setZ(0).toVector(), 100);

                if (search.overlaps(s)) {
                    if (!this.pendingLocationRequests.containsKey(n.uuid)) {
                        // Is within zone.
                        connection.sendTcp("nray|" + n.location + "|" + n.uuid.toString());
                        pendingLocationRequests.put(UUID.fromString(n.uuid), Pair.of(now + TimeUnit.SECONDS.toMillis(30), n));
                    }
                }

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void processSpawnRayReply(UUID uuid, Location location) {
        if (this.pendingLocationRequests.containsKey(uuid)) {
            // Has a pending request.

            Pair<Long, GameResourceNode> p = this.pendingLocationRequests.get(uuid);
            if (System.currentTimeMillis() <= p.getLeft()) {
                // Is valid

                try {
                    GameResourceNode n = DataService.resourceNodes.queryForId(uuid.toString());

                    if (n != null) {
                        // Is a valid node
                        n.realLocation = n.location.cpy().setZ(location.getZ());

                        n.spawnEntity.location = n.realLocation;
                        n.spawnEntity.setMeta("rn", n.uuid.toString());

                        // Spawn the entity
                        GameEntityModel entityModel = DedicatedServer.instance.getWorld().spawn(n.spawnEntity, n.realLocation);

                        n.spawned = true;
                        n.attachedEntity = entityModel.uuid;
                        DataService.resourceNodes.update(n);

//                        System.out.println("Spawned Net Entity");

                    } else {
                        System.out.println("Invalid UUID");
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            } else {
                System.out.println("Invalid Request");
                this.pendingLocationRequests.remove(uuid);
            }

        } else {
            System.out.println("No Request Pending.");
        }
    }

    @Override
    public void init() {

    }
}
