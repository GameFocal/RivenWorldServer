package com.gamefocal.rivenworld.threads;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.*;
import io.airbrake.javabrake.Airbrake;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@AsyncThread(name = "world-state")
public class WorldStateThread implements HiveAsyncThread {

    public static Long lastSave = 0L;

    @Override
    public void run() {
        while (true) {
            try {
                if (DedicatedServer.instance.getWorld() != null) {

                    for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                        if (connection != null) {
//                        if (!connection.isSyncUpdates()) {
//                            continue;
//                        }
//
//                        if (DedicatedServer.get(CharacterCustomizationService.class).isInCreation(connection)) {
//                            // TODO: Other things for char creation here
//                            DedicatedServer.get(EnvironmentService.class).emitOverrideEnvironmentChange(connection, true, .25f, GameWeather.CLEAR);
//                            continue;
//                        }

                            if (EnvironmentService.isFreezeTime()) {
                                DedicatedServer.get(EnvironmentService.class).emitEnvironmentChange(connection, true);
                            }

                            // Sync Foliage
                            try {
                                for (GameFoliageModel foliageModel : DataService.gameFoliage.queryForAll()) {
                                    String currentHash = foliageModel.stateHash();
                                    String syncHash = "NONE";

                                    if (connection.getFoliageSync().containsKey(foliageModel.uuid)) {
                                        syncHash = connection.getFoliageSync().get(foliageModel.uuid);
                                    }

                                    if (!currentHash.equalsIgnoreCase(syncHash)) {
                                        // Does not equal emit the sync.
                                        foliageModel.syncToPlayer(connection, true);
                                    }
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }

//                        // Game Entites
//                        for (Map.Entry<UUID, WorldChunk> e: DedicatedServer.instance.getWorld().entityChunkIndex.entrySet()) {
//                            e.getValue().getEntityModelFromUUID(e.getKey()).syncState(connection);
//                        }

//                            for (WorldChunk c : connection.getChunksInRenderDistance(connection.getRenderDistance())) {
//                                connection.syncChunk(c);
//                            }
////
//                            BoundingBox searchBox = ShapeUtil.makeBoundBox(connection.getPlayer().location.cpy().setZ(0).toVector(), connection.getRenderDistance(), 60000);
//
//                            for (String chunkCord : connection.getLoadedChunks().keySet()) {
//
//                                Location chunkCords = Location.fromString(chunkCord);
//
//                                if (chunkCords != null) {
//                                    WorldChunk c = DedicatedServer.instance.getWorld().getChunk(chunkCords.getX(), chunkCords.getY());
////                            connection.drawDebugBox(c.getBoundingBox(),2);
//
//                                    if (searchBox.contains(c.getBoundingBox()) || searchBox.intersects(c.getBoundingBox())) {
//                                        connection.updateChunk(c);
//                                    }
//                                }
//
////                            if (this.loadedChunks.containsKey(chunk.getChunkCords().toString())) {
////                                // Should update it
////                                this.updateChunk(chunk);
////                            } else {
////                                this.drawDebugBox(chunk.getBoundingBox(), 5);
////                                this.loadChunk(chunk);
////                            }
//                            }

                            // Resource Nodes
                            DedicatedServer.get(ResourceService.class).spawnNearbyNodes(connection, connection.getRenderDistance());

                            for (WorldChunk[] chunks : DedicatedServer.instance.getWorld().getChunks()) {
                                for (WorldChunk chunk : chunks) {
                                    // Loop through each chunk

                                    boolean inView = connection.isChunkIsView(chunk);
                                    boolean isLoaded = connection.getLoadedChunks().containsKey(chunk.getChunkCords().toString());

                                    if (inView && !isLoaded) {
                                        // Is in view but not loaded
                                        connection.subscribeToChunk(chunk);
                                    } else if (isLoaded && !inView) {
                                        // Is loaded but no longer in view
                                        connection.unsubscribeToChunk(chunk);
                                    } else if (inView && isLoaded) {
                                        // Is loaded and in view, update entites
                                        for (GameEntityModel entityModel : chunk.getEntites().values()) {
                                            connection.syncEntity(entityModel, chunk, false);
                                        }
                                    }
                                }
                            }

//                            // Despawn chunks currently loaded if not in-view
//                            for (String cords : connection.getLoadedChunks().keySet()) {
//
//                                Location cl = Location.fromString(cords);
//
//                                WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(cl.getX(), cl.getY());
//                                if (chunk != null) {
//                                    // The chunk is real
//                                    if (!connection.isChunkIsView(chunk)) {
//                                        connection.unsubscribeToChunk(chunk);
//                                    }
//                                }
//                            }
//
//                            // Sync Entites in chunks nearby
//                            for (WorldChunk chunk : connection.getChunksInRenderDistance(connection.getRenderDistance())) {
//
//                            }

                            new ServerWorldSyncEvent(connection).call();

                            // Send sync udp packet
                            connection.sendSyncPackage();

                            connection.sendAttributes();

                            // See is dead
                            if (connection.getPlayer().playerStats.health <= 0 && !connection.getState().isDead) {
                                DedicatedServer.get(RespawnService.class).killPlayer(connection, null);
                            }
                        }

                        // Processing Pending Rays
                        DedicatedServer.get(RayService.class).processPendingReqs();
                    }

                    if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastSave) >= 5) {
                        if (DedicatedServer.instance.getWorld() != null) {
                            DedicatedServer.instance.getWorld().save();
                            lastSave = System.currentTimeMillis();
                        }
                    }
                }

            } catch (Exception e) {
                Airbrake.report(e);
                e.printStackTrace();
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
