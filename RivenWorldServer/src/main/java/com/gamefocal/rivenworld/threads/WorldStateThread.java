package com.gamefocal.rivenworld.threads;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.game.World;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.*;
import io.airbrake.javabrake.Airbrake;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@AsyncThread(name = "world-state")
public class WorldStateThread implements HiveAsyncThread {

    public static Long lastSave = 0L;
    public static ConcurrentHashMap<UUID, Long> lastPingMsg = new ConcurrentHashMap<>();

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

                            if (lastPingMsg.containsKey(connection.getUuid())) {
                                if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastPingMsg.get(connection.getUuid())) > 15) {
                                    connection.sendTcp("p|");
                                    connection.sendUdp("p|");
                                    lastPingMsg.put(connection.getUuid(), System.currentTimeMillis());
                                }
                            } else {
                                connection.sendTcp("p|");
                                connection.sendUdp("p|");
                                lastPingMsg.put(connection.getUuid(), System.currentTimeMillis());
                            }

                            if (!connection.isGetAutoWorldSyncUpdates()) {
                                continue;
                            }

//                            connection.sendUdp("p|");
//                            connection.sendTcp("p|");

                            if (!connection.connectionIsAlive()) {
                                System.out.println("Cleaning up connection...");
                                connection.kick("Network Timeout");
                            }

                            if (EnvironmentService.isFreezeTime()) {
                                DedicatedServer.get(EnvironmentService.class).emitEnvironmentChange(connection, true);
                            }

                            // Sync Foliage
//                            try {
                            for (GameFoliageModel foliageModel : DedicatedServer.get(FoliageService.class).getFoliage().values()) {
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
//                            } catch (SQLException throwables) {
//                                throwables.printStackTrace();
//                            }

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

                    // Check for ownerships
                    DedicatedServer.get(PeerVoteService.class).processOwnerships();

                    // Tree Growth
                    if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - FoliageService.lastTreeGrowth) >= 30) {
                        new Thread(() -> {
                            DedicatedServer.get(FoliageService.class).growTick();
                        }).start();
                        FoliageService.lastTreeGrowth = System.currentTimeMillis();
                    }

                    // Decay
                    if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - DecayService.lastDecay) >= 60) {
                        new Thread(() -> {
                            for (WorldChunk[] cc : DedicatedServer.instance.getWorld().getChunks()) {
                                for (WorldChunk c : cc) {
                                    DedicatedServer.get(DecayService.class).processDecay(c);
                                }
                            }

                        }).start();
                        DecayService.lastDecay = System.currentTimeMillis();
                    }

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
                Thread.yield();
                e.printStackTrace();
            }
        }
    }
}
