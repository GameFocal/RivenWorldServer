package com.gamefocal.rivenworld.threads;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.game.NetWorldSyncPackage;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.world.World;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.*;
import io.airbrake.javabrake.Airbrake;
import lowentry.ue4.library.LowEntry;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@AsyncThread(name = "world-state")
public class WorldStateThread implements HiveAsyncThread {

    public static Long lastSave = 0L;
    public static Long lastNodeRespawn = 0L;
    public static ConcurrentHashMap<UUID, Long> lastPingMsg = new ConcurrentHashMap<>();

    @Override
    public void run() {

        long sleepTime = 1;
        long start = 0L;
        long deltaTime = 0;

        while (true) {
            start = System.currentTimeMillis();
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
//                            System.out.println("NODES");
//                            DedicatedServer.get(ResourceService.class).spawnNearbyNodes(connection, connection.getRenderDistance());

//                            connection.syncChunkLODs(false, true, syncPackage);

//                            for (WorldChunk[] chunks : DedicatedServer.instance.getWorld().getChunks()) {
//                                for (WorldChunk chunk : chunks) {
//                                    boolean chunkDirty = true;
//                                    if (connection.chunkVersions.containsKey(chunk.getChunkCords().toString())) {
//                                        if (connection.chunkVersions.get(chunk.getChunkCords().toString()) == chunk.version) {
//                                            chunkDirty = false;
//                                        }
//                                    }
//
//                                    if (chunkDirty) {
//                                        int before = syncPackage.operationCount();
//                                        connection.syncChunkLOD(chunk, false, true, syncPackage);
//                                        if (syncPackage.operationCount() > before) {
//                                            connection.chunkVersions.put(chunk.getChunkCords().toString(), chunk.version);
//                                        }
//                                    }
//                                }
//                            }

//                            List<GameEntity> nearby = DedicatedServer.instance.getWorld().getCollisionManager().getNearbyEntities(connection.getPlayer().location);
//                            ArrayList<WorldChunk> nearbyChunks = new ArrayList<>();
//                            for (GameEntity ne : nearby) {
//                                WorldChunk chunk = ne.getChunk();
//                                if (chunk != null) {
//                                    if (!nearbyChunks.contains(chunk)) {
//                                        nearbyChunks.add(chunk);
//                                        connection.syncChunkLOD(chunk, false, true, syncPackage);
//                                    }
//                                }
//                            }

//                            System.out.println("Chunks: " + nearbyChunks.size());

                            NetWorldSyncPackage syncPackage = new NetWorldSyncPackage();
                            connection.syncChunkLODs(false, true, syncPackage);

//                            for (WorldChunk[] chunks : DedicatedServer.instance.getWorld().getChunks()) {
//                                for (WorldChunk chunk : chunks) {
//                                    NetWorldSyncPackage syncPackage = new NetWorldSyncPackage();
//                                    connection.syncChunkLOD(chunk, false, true, syncPackage);
//                                    if (syncPackage.hasData()) {
//                                        // Send via tcp
//                                        connection.sendTcp("wsync|" + LowEntry.bytesToBase64(syncPackage.getJson().toString().getBytes()));
//                                    }
//                                }
//                            }

                            new ServerWorldSyncEvent(connection).call();

                            // Send sync udp packet
                            connection.sendSyncPackage();

                            // Calc max speed
//                            float maxSpeed = connection.calcMaxSpeed();
//                            connection.SetSpeed(maxSpeed);

                            // Send Health etc.
                            connection.sendAttributes();

                            // See is dead
                            if (connection.getPlayer().playerStats.health <= 0 && !connection.getState().isDead) {
                                DedicatedServer.get(RespawnService.class).killPlayer(connection, null);
                            }

                            // Check if in a chunk in combat

                            WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(connection.getPlayer().location);

                            boolean inCombatMusic = false;

                            if (chunk != null && chunk.inCombat()) {
                                inCombatMusic = true;
                            } else {
                                // Combat time
                                if (connection.inCombat()) {
                                    inCombatMusic = true;
                                }
                            }

                            if (inCombatMusic) {
                                if (connection.getBgSound() != GameSounds.Battle) {
                                    connection.playBackgroundSound(GameSounds.Battle, 1, 1);
                                }
                            } else {
                                if (connection.getBgSound() == GameSounds.Battle) {
                                    connection.syncToAmbientWorldSound();
                                }
                            }
                        }

                        // Processing Pending Rays
//                        DedicatedServer.get(RayService.class).processPendingReqs();
                    }

                    // Vote Checkup
                    DedicatedServer.get(PeerVoteService.class).monitorVotes();

//                    // Check for ownerships
//                    DedicatedServer.get(PeerVoteService.class).processOwnerships();

                    // Tree Growth
                    if (DedicatedServer.isReady) {

                        // Spawn due resource spawns
                        // Respawn Nodes
                        if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastNodeRespawn) >= 1) {
                            DedicatedServer.get(ResourceService.class).checkForRespawns();
                        }

                        if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - FoliageService.lastTreeGrowth) >= 30) {
                            new Thread(() -> {
                                System.out.println("[Trees]: Starting Growth");
                                DedicatedServer.get(FoliageService.class).growTick();
                                System.out.println("[Trees]: Complete");
                            }).start();
                            FoliageService.lastTreeGrowth = System.currentTimeMillis();
                        }

                        // Decay
                        if (TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - DecayService.lastDecay) >= 24) {
                            new Thread(() -> {

                                System.out.println("[World Decay]: Starting Decay");

                                for (WorldChunk[] cc : DedicatedServer.instance.getWorld().getChunks()) {
                                    for (WorldChunk c : cc) {
                                        DedicatedServer.get(DecayService.class).processDecay(c);
                                    }
                                }

                                System.out.println("[World Decay]: Complete");
                            }).start();
                            DecayService.lastDecay = System.currentTimeMillis();
                        }

//                        // Ground Layer Respawn
//                        if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - ResourceService.lastGroundLayerRespawn) >= DedicatedServer.settings.groundLayerRespawnTimeInMinutes) {
//                            // Respawn ground layer
//                            ResourceService.lastGroundLayerRespawn = System.currentTimeMillis();
//                            new Thread(() -> {
//                                DedicatedServer.get(ResourceService.class).respawnGroundNodes();
//                            }).start();
//                            ResourceService.lastGroundLayerRespawn = System.currentTimeMillis();
//                        }

                        if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastSave) >= 5) {
                            SaveService.saveGame();
                            lastSave = System.currentTimeMillis();
                        }
                    }
                }

                deltaTime = System.currentTimeMillis() - start;

                if (deltaTime < 5) {
                    sleepTime = (5 - deltaTime);
                }

//                System.out.println("SLEEP: " + sleepTime);

            } catch (Exception e) {
                Airbrake.report(e);
                e.printStackTrace();
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.yield();
                e.printStackTrace();
            }
        }
    }
}
