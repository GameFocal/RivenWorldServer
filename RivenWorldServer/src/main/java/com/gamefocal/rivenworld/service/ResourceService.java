package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.events.resources.DestroyResourceNodeEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.entites.resources.ground.GroundStickEntity;
import com.gamefocal.rivenworld.game.entites.resources.ground.SmallRockEntity;
import com.gamefocal.rivenworld.game.entites.resources.ground.ThatchBush;
import com.gamefocal.rivenworld.game.generator.WorldLayerGenerator;
import com.gamefocal.rivenworld.game.generator.basic.FiberLayer;
import com.gamefocal.rivenworld.game.generator.basic.SmallRockLayer;
import com.gamefocal.rivenworld.game.generator.basic.StickLayer;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameResourceNode;
import com.google.auto.service.AutoService;
import com.j256.ormlite.stmt.QueryBuilder;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
@AutoService(HiveService.class)
public class ResourceService implements HiveService<ResourceService> {

    public static long lastGroundLayerRespawn = 0L;
    public ArrayList<Location> pendingLocations = new ArrayList<>();
    public HashMap<Class<? extends GameEntity>, WorldLayerGenerator> autoRefreshLayers = new HashMap<>();

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

    public void oneOffNodeHarvest(GameEntity entity) {
        try {
            GameResourceNode resourceNode = DataService.resourceNodes.queryBuilder().where().eq("attachedEntity", entity.uuid).queryForFirst();

            if (resourceNode != null) {
                DedicatedServer.instance.getWorld().despawn(resourceNode.attachedEntity);

                // Process the death of the node
                resourceNode.spawned = false;
                resourceNode.attachedEntity = null;
                resourceNode.nextSpawn = (System.currentTimeMillis() + (TimeUnit.MINUTES.toMillis((long) Math.floor(resourceNode.spawnDelay))));

                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.BreakNode, entity.location, 300, 1f, 1f);

                try {
                    DataService.resourceNodes.update(resourceNode);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNode(ResourceNodeEntity entity, Location location, int respawnTimeInMins) {
        GameResourceNode resourceNode = new GameResourceNode();
        resourceNode.uuid = UUID.randomUUID().toString();
        resourceNode.location = location;
        try {
            resourceNode.spawnEntity = entity.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        resourceNode.spawnDelay = respawnTimeInMins;

        this.spawnFromResourceNodePoint(resourceNode);

//        try {
//            DataService.resourceNodes.createOrUpdate(resourceNode);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
    }

    public void removeNode(ResourceNodeEntity nodeEntity) {
        try {
            GameResourceNode resourceNode = DataService.resourceNodes.queryBuilder().where().eq("attachedEntity", nodeEntity.uuid).queryForFirst();

            if (resourceNode != null) {
                DedicatedServer.instance.getWorld().despawn(nodeEntity.uuid);
                DataService.resourceNodes.delete(resourceNode);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void harvest(EntityHitResult hitResult, ResourceNodeEntity entity, HiveNetConnection connection) {
        try {
            GameResourceNode resourceNode = DataService.resourceNodes.queryBuilder().where().eq("attachedEntity", entity.uuid).queryForFirst();

            if (resourceNode != null) {
                InventoryStack inHand = connection.getPlayer().equipmentSlots.inHand;

                if (entity.getAllowedTools().size() > 0) {
                    if (!entity.isAllowedTool(inHand.getItem().getClass())) {
                        return;
                    }
                }

                if (!connection.inHandDurability(2)) {
                    return;
                }

                connection.setAnimationCallback((connection1, args) -> {
                    if (entity.health <= 0) {
                        return;
                    }

                    float damage = 0.0f;
                    if (inHand == null) {
                        damage = 1.0f;
                    } else {
                        if (ToolInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {
                            ToolInventoryItem tool = (ToolInventoryItem) inHand.getItem();
                            damage = tool.hit();
                        }
                    }

                    // TODO: Apply a stats multipule here for buffs

                    entity.health -= 5;

                    connection.flashProgressBar(entity.getClass().getSimpleName(), entity.health / entity.maxHealth, Color.RED, 5);

                    connection.showFloatingTxt("-" + damage, entity.location.cpy().addZ(100));
                    DedicatedServer.instance.getWorld().playSoundAtLocation(entity.hitSound, entity.location, 300, 1.5f, .5f);

                    if (entity.health <= 0) {

//                        int minSpawn = Math.round(resourceNode.spawnDelay);
//                        int maxSpawn = (int) (Math.round(resourceNode.spawnDelay) + (Math.round(resourceNode.spawnDelay) * .35));

                        DedicatedServer.instance.getWorld().despawn(resourceNode.attachedEntity);

                        // Process the death of the node
                        resourceNode.spawned = false;
                        resourceNode.attachedEntity = null;
                        resourceNode.nextSpawn = (System.currentTimeMillis() + (TimeUnit.MINUTES.toMillis((long) Math.floor(resourceNode.spawnDelay))));

                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.BreakNode, entity.location, 300, 1f, 1f);

                        new DestroyResourceNodeEvent(connection, entity, resourceNode).call();

                        try {
                            DataService.resourceNodes.update(resourceNode);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    } else if (entity.giveProgressiveDrops) {
                        InventoryStack d = RandomUtil.getRandomElementFromArray(entity.drops());
                        int a = d.getAmount();
                        int g = (int) RandomUtil.getRandomNumberBetween(1, Math.max(1, (a * (damage / 10))));
                        d.setAmount(g);

                        // Give the item
                        connection.getPlayer().inventory.add(d);
                        connection.displayItemAdded(d);
                    }
                });

//                connection.playAnimation(entity.hitAnimation);
                connection.playAnimation(entity.hitAnimation, "DefaultSlot", 1.5F, 0, -1, 0.25f, 0.25f, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkForRespawns() {
        DataService.exec(() -> {
            QueryBuilder<GameResourceNode, String> q = DataService.resourceNodes.queryBuilder();
            try {
                long now = System.currentTimeMillis();
                q.where().eq("spawned", false).and().isNotNull("realLocation");
                List<GameResourceNode> nodes = q.query();

                for (GameResourceNode node : nodes) {
                    if (node != null && node.nextSpawn <= System.currentTimeMillis()) {
                        GameEntityModel entityModel = DedicatedServer.instance.getWorld().spawn(node.spawnEntity, node.realLocation);

                        node.spawned = true;
                        node.attachedEntity = entityModel.uuid;
                        try {
                            DataService.resourceNodes.update(node);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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

                            n.realLocation = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(n.location);
                            n.spawnEntity.location = n.realLocation;
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


//                            if (!this.pendingLocations.contains(n.location)) {
//                                this.pendingLocations.add(n.location);
//
////                                DedicatedServer.get(RayService.class).makeRequest(n.location, 1, request -> {
////                                    // Spawn the node here :)
////                                    n.realLocation = n.location.cpy().setZ(request.getReturnedLocation().getZ());
////
////                                    n.spawnEntity.location = n.realLocation;
////                                    n.spawnEntity.setMeta("rn", n.uuid.toString());
////
////                                    // Spawn the entity
////                                    GameEntityModel entityModel = DedicatedServer.instance.getWorld().spawn(n.spawnEntity, n.realLocation);
////
////                                    n.spawned = true;
////                                    n.attachedEntity = entityModel.uuid;
////                                    try {
////                                        DataService.resourceNodes.update(n);
////                                        this.pendingLocations.remove(n.location);
////                                    } catch (SQLException throwables) {
////                                        throwables.printStackTrace();
////                                    }
////                                });
//                            }
                        }
                    }
                }

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void spawnFromResourceNodePoint(GameResourceNode resourceNode) {
//        DataService.exec(() -> {
        resourceNode.realLocation = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(resourceNode.location);

        resourceNode.spawnEntity.uuid = UUID.randomUUID();
        resourceNode.attachedEntity = resourceNode.spawnEntity.uuid;
        resourceNode.spawned = true;

        try {
            DataService.resourceNodes.createOrUpdate(resourceNode);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DedicatedServer.instance.getWorld().spawn(resourceNode.spawnEntity, resourceNode.realLocation);
//        });
    }

    public void resetGroundNodes() {
        System.out.println("[Ground Layer Respawn]: Starting ground layer respawn...");

        DataService.exec(() -> {
            System.out.println("[Ground Layer Respawn]: Clearing current nodes");
            for (UUID uuid : DedicatedServer.instance.getWorld().entityChunkIndex.keySet()) {
                GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(uuid);
                if (e != null) {
                    for (Class<? extends GameEntity> c : this.autoRefreshLayers.keySet()) {
                        if (c.isAssignableFrom(e.entityData.getClass())) {
                            // Is a layer item
                            DedicatedServer.instance.getWorld().despawn(uuid);
                        }
                    }
                }
            }
        });

        /*
         * Respawn using the nodelist
         * */
        DataService.exec(() -> {
            try {
                List<GameResourceNode> nodes = DataService.resourceNodes.queryForAll();

                System.out.println("[Ground Layer Respawn]: Clearing the ground layer");

                int i = 0;

                for (GameResourceNode node : nodes) {
                    for (Class<? extends GameEntity> c : this.autoRefreshLayers.keySet()) {
                        if (c.isAssignableFrom(node.spawnEntity.getClass())) {
                            DataService.resourceNodes.delete(node);
                            break;
                        }
                    }
                }

                /*
                 * Regen the layer
                 * */
                for (WorldLayerGenerator generator : this.autoRefreshLayers.values()) {
                    System.out.println("[Ground Layer Respawn]: Generating the layer " + generator.getClass().getSimpleName());
                    generator.generateLayer(DedicatedServer.instance.getWorld());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void respawnGroundNodes() {
        if (DedicatedServer.isReady) {
            System.out.println("[Ground Layer Respawn]: Starting ground layer respawn...");

            DataService.exec(() -> {
                System.out.println("[Ground Layer Respawn]: Clearing current nodes");
                for (UUID uuid : DedicatedServer.instance.getWorld().entityChunkIndex.keySet()) {
                    GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(uuid);
                    if (e != null) {
                        for (Class<? extends GameEntity> c : this.autoRefreshLayers.keySet()) {
                            if (c.isAssignableFrom(e.entityData.getClass())) {
                                // Is a layer item
                                DedicatedServer.instance.getWorld().despawn(uuid);
                            }
                        }
                    }
                }
            });

            /*
             * Respawn using the nodelist
             * */
            DataService.exec(() -> {
                try {
                    List<GameResourceNode> nodes = DataService.resourceNodes.queryForAll();

                    System.out.println("[Ground Layer Respawn]: Regenerating ground layer");

                    int i = 0;

                    for (GameResourceNode node : nodes) {
                        for (Class<? extends GameEntity> c : this.autoRefreshLayers.keySet()) {
                            if (c.isAssignableFrom(node.spawnEntity.getClass())) {
                                // Is one we're looking for
                                if (node.realLocation != null) {
                                    i++;
                                    // has been spawned int he world space before
                                    node.spawnEntity.location = node.realLocation;
                                    node.spawnEntity.setMeta("rn", node.uuid.toString());
                                    node.spawnEntity.uuid = null;

                                    // Spawn the entity
                                    GameEntityModel entityModel = DedicatedServer.instance.getWorld().spawn(node.spawnEntity, node.realLocation);

                                    node.spawned = true;
                                    node.attachedEntity = entityModel.uuid;
                                    try {
                                        DataService.resourceNodes.update(node);
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    System.out.println("[Ground Layer Respawn]: Complete populated " + i + " ground layer nodes.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void init() {
        this.autoRefreshLayers.put(ThatchBush.class, new FiberLayer());
        this.autoRefreshLayers.put(SmallRockEntity.class, new SmallRockLayer());
        this.autoRefreshLayers.put(GroundStickEntity.class, new StickLayer());
    }
}
