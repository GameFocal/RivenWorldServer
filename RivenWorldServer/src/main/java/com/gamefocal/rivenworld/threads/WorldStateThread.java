package com.gamefocal.rivenworld.threads;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.game.World;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.*;
import io.airbrake.javabrake.Airbrake;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@AsyncThread(name = "world-state")
public class WorldStateThread implements HiveAsyncThread {

    public static Long lastSave = 0L;

    @Override
    public void run() {
        while (true) {
            try {
                if (DedicatedServer.instance.getWorld() != null) {

                    if (DedicatedServer.isReady && World.pendingWorldLoads.size() > 0) {
                        while (World.pendingWorldLoads.size() > 0) {
                            UUID pid = World.pendingWorldLoads.poll();
                            if (pid != null && DedicatedServer.get(PlayerService.class).players.containsKey(pid)) {
                                DedicatedServer.instance.getWorld().loadWorldForPlayer(DedicatedServer.get(PlayerService.class).players.get(pid));
                            }
                        }
                    }

                    for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                        if (connection != null) {

                            if (!connection.isGetAutoWorldSyncUpdates()) {
                                continue;
                            }

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

                            connection.syncChunkLODs(false, true);

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

                    // Vote Checkup
                    DedicatedServer.get(PeerVoteService.class).monitorVotes();

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
                Airbrake.report(e);
            }
        }
    }
}
