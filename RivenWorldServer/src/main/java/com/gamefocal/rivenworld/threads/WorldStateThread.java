package com.gamefocal.rivenworld.threads;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.game.WorldChunk;
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
                                            connection.syncEntity(entityModel, chunk, false,true);
                                        }
                                    }
                                }
                            }

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

                    // Spawn due resource spawns
                    // Respawn Nodes
                    DedicatedServer.get(ResourceService.class).checkForRespawns();

                    if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastSave) >= 5) {
                        SaveService.saveGame();
                        lastSave = System.currentTimeMillis();
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
