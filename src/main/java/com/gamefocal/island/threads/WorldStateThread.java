package com.gamefocal.island.threads;

import com.badlogic.gdx.utils.compression.lzma.Base;
import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.thread.AsyncThread;
import com.gamefocal.island.entites.thread.HiveAsyncThread;
import com.gamefocal.island.events.game.ServerWorldSyncEvent;
import com.gamefocal.island.game.player.PlayerState;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.PlayerService;
import com.gamefocal.island.service.RayService;
import com.gamefocal.island.service.ResourceService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@AsyncThread(name = "world-state")
public class WorldStateThread implements HiveAsyncThread {
    @Override
    public void run() {
        while (true) {
            if (DedicatedServer.instance.getWorld() != null) {

                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {

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

                    // Game Entites
                    for (GameEntityModel model : DedicatedServer.instance.getWorld().entites.values()) {
                        model.syncState(connection);
                    }

                    // Resource Nodes
                    DedicatedServer.get(ResourceService.class).spawnNearbyNodes(connection, 20 * 100 * 4);

                    new ServerWorldSyncEvent(connection).call();
                }

                // Processing Pending Rays
                DedicatedServer.get(RayService.class).processPendingReqs();

            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
