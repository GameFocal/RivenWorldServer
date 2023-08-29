package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.exception.RWThreadFactory;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.events.game.ServerTickEvent;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.events.player.PlayerStateSyncEvent;
import com.gamefocal.rivenworld.game.NetWorldSyncPackage;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.ui.CraftingUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.world.World;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.google.auto.service.AutoService;
import io.airbrake.javabrake.Airbrake;

import javax.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@AutoService(HiveService.class)
@Singleton
public class GameTickService implements HiveService<GameTickService> {

    public static long lastSave = 0L;

    private ConcurrentHashMap<UUID, Long> playerSpawnDelay = new ConcurrentHashMap<>();

    // Desired ticks per second
    private static final int TPS = 20;
    // Time per tick in milliseconds
    private static final long TICK_TIME = 1000 / TPS;

    private ScheduledExecutorService gameTickExecutor;

    private ScheduledExecutorService playerTickExecutor;

    private ScheduledExecutorService worldTickExecutor;

    private ScheduledExecutorService aiTickExecutor;

    private ScheduledExecutorService saveGameExecutor;

    private ScheduledExecutorService hiveExecutor;
    public static Long lastNodeRespawn = 0L;
    public static ConcurrentHashMap<UUID, Long> lastPingMsg = new ConcurrentHashMap<>();
    public static long tps = 0;

    @Override
    public void init() {
        this.gameTickExecutor = Executors.newSingleThreadScheduledExecutor(new RWThreadFactory());
        this.startGameTick();

        this.playerTickExecutor = Executors.newSingleThreadScheduledExecutor(new RWThreadFactory());
        this.startPlayerTick();

        this.worldTickExecutor = Executors.newSingleThreadScheduledExecutor(new RWThreadFactory());
        this.startWorldTIck();

        this.aiTickExecutor = Executors.newSingleThreadScheduledExecutor(new RWThreadFactory());
        this.startAiTick();

        this.saveGameExecutor = Executors.newSingleThreadScheduledExecutor(new RWThreadFactory());
        this.startSaveGameTick();

        this.hiveExecutor = Executors.newSingleThreadScheduledExecutor(new RWThreadFactory());
        this.startHiveTick();
    }

    public void startHiveTick() {
        hiveExecutor.scheduleAtFixedRate(() -> {
            DedicatedServer.licenseManager.hb();
        },0,15,TimeUnit.SECONDS);
    }

    public void startSaveGameTick() {
        saveGameExecutor.scheduleAtFixedRate(()->{
            try {

                /*
                 * Poll entities
                 * */
                if ((!SaveService.allowNewSaves && SaveService.saveQueue.size() > 0) || (DedicatedServer.isReady && TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastSave) >= 15)) {

                    int size = SaveService.saveQueue.size();


                    for (int i = 0; i < size; i++) {
                        GameEntityModel model = SaveService.saveQueue.poll();
                        if (model != null) {
                            DataService.gameEntities.createOrUpdate(model);
                        }
                    }

                    /*
                     * Save Shops and other items every minute
                     * */
//                    if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - SaveService.lastOtherSave) >= 60) {
//                        SaveService.lastOtherSave = System.currentTimeMillis();
                    SaveService.syncNonEntities();

                    lastSave = System.currentTimeMillis();
//                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Airbrake.report(e);
            }
        },0,5,TimeUnit.MINUTES);
    }

    public void startAiTick() {
        aiTickExecutor.scheduleAtFixedRate(() -> {
            try {
                // AI Tick
                DedicatedServer.get(AiService.class).processAiTick();
            } catch (Exception e) {
                e.printStackTrace();
                Airbrake.report(e);
            }
        }, 0, 25, TimeUnit.MILLISECONDS);
    }

    public void startWorldTIck() {
        worldTickExecutor.scheduleAtFixedRate(() -> {

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

                            connection.validateStateEffects();

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

                            if (!connection.connectionIsAlive()) {
                                System.out.println("Cleaning up connection...");
                                connection.kick("Network Timeout");
                            }

                            if (EnvironmentService.isFreezeTime()) {
                                DedicatedServer.get(EnvironmentService.class).emitEnvironmentChange(connection, true);
                            }

                            // Sync Foliage
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

                            NetWorldSyncPackage syncPackage = new NetWorldSyncPackage();
                            connection.syncChunkLODs(false, true, syncPackage);

                            if (syncPackage.hasData()) {
                                connection.sendWorldStateSyncPackage(syncPackage);
                            }

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
                    }

                    // Vote Checkup
                    DedicatedServer.get(PeerVoteService.class).monitorVotes();


                    /*
                     * World Tasks
                     * */
                    if (DedicatedServer.isReady) {

                        // Spawn due resource spawns
                        // Respawn Nodes
                        if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastNodeRespawn) >= 1) {
                            DedicatedServer.get(ResourceService.class).checkForRespawns();
                            lastNodeRespawn = System.currentTimeMillis();
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
                    }
                }

//                System.out.println("SLEEP: " + sleepTime);

            } catch (Exception e) {
                Airbrake.report(e);
                e.printStackTrace();
            }

        }, 0, TICK_TIME, TimeUnit.MILLISECONDS);
    }

    public void startPlayerTick() {
        playerTickExecutor.scheduleAtFixedRate(() -> {
            try {

                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {

                    if (connection.getDragging() != null) {
                        Vector3 dragPoint = connection.getPlayer().location.toVector();
                        dragPoint.mulAdd(connection.getForwardVector(), 100);
                        dragPoint.z += 50;
                        connection.getDragging().tpToLocation(Location.fromVector(dragPoint));
//                        connection.getDragging().getPlayer().location = Location.fromVector(dragPoint);
                        DedicatedServer.get(PlayerService.class).players.get(connection.getDragging().getUuid()).getPlayer().location = Location.fromVector(dragPoint);
                    }

                    for (HiveNetConnection peer : DedicatedServer.get(PlayerService.class).players.values()) {
                        if (!peer.getPlayer().id.equalsIgnoreCase(connection.getPlayer().id)) {

//                            if (!peer.isLoaded()) {
//                                continue;
//                            }

                            boolean isNearby = false;
                            boolean isLoaded = false;
                            boolean isDead = peer.getState().isDead;
                            boolean isHidden = !peer.isVisible();

                            isLoaded = connection.getLoadedPlayers().containsKey(peer.getUuid());
                            isNearby = (connection.getLOD(peer.getPlayer().location) <= 1);

                            if (isLoaded && (isHidden || isDead)) {
                                connection.sendHidePacket(peer);
                                connection.getLoadedPlayers().remove(peer.getUuid());
                                playerSpawnDelay.remove(peer.getUuid());
                                continue;
                            }

                            if (isHidden) {
                                continue;
                            }

                            if (isLoaded && !isNearby) {
                                connection.sendHidePacket(peer);
                                connection.getLoadedPlayers().remove(peer.getUuid());
                                playerSpawnDelay.remove(peer.getUuid());
                            } else if (!isLoaded && isNearby) {
                                connection.getLoadedPlayers().put(peer.getUuid(), "fresh");
                            }

                            if (connection.getLoadedPlayers().containsKey(peer.getUuid())) {

//                                boolean firstLoad = false;
//                                if (connection.getLoadedPlayers().get(peer.getUuid()).equalsIgnoreCase("fresh")) {
//                                    firstLoad = true;
//                                }

                                if (!connection.getLoadedPlayers().get(peer.getUuid()).equalsIgnoreCase(peer.playStateHash())) {
                                    peer.getState().tick();

                                    connection.sendStatePacket(peer);
                                    connection.getLoadedPlayers().put(peer.getUuid(), peer.playStateHash());
                                }
                            }
                        }
                    }

                    new PlayerStateSyncEvent(connection).call();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, TICK_TIME, TimeUnit.MILLISECONDS);
    }

    public void startGameTick() {
        gameTickExecutor.scheduleAtFixedRate(() -> {
            try {

                /*
                 * Run the tasks here
                 * */
                DedicatedServer.get(TaskService.class).tick();

                new ServerTickEvent().call();

                if (DedicatedServer.instance.getWorld() != null) {

                    // Tick Entites
                    for (UUID uuid : DedicatedServer.instance.getWorld().tickEntites) {
                        try {
                            GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(uuid);

                            if (e == null) {
                                DedicatedServer.instance.getWorld().tickEntites.remove(uuid);
                                continue;
                            }

                            e.entityData.onTick();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // Player Inventories
                    for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                        if (connection.getPlayer().inventory.canCraft()) {
                            if (connection.getPlayer().inventory.getCraftingQueue().tick(connection)) {
                                // Has a job that has been completed
//                                connection.updateInventory(connection.getPlayer().inventory);
                                if (connection.getOpenUI() != null && CraftingUI.class.isAssignableFrom(connection.getOpenUI().getClass())) {
                                    connection.getOpenUI().update(connection);
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                // Handle any exceptions that might occur during event processing
                e.printStackTrace();
            }
        }, 0, TICK_TIME, TimeUnit.MILLISECONDS);
    }
}
