package com.gamefocal.island.threads;

import com.badlogic.gdx.utils.compression.lzma.Base;
import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.thread.AsyncThread;
import com.gamefocal.island.entites.thread.HiveAsyncThread;
import com.gamefocal.island.game.player.PlayerState;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.PlayerService;
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

                // Sync Foliage Items
                try {
                    for (GameFoliageModel foliageModel : DataService.gameFoliage.queryForAll()) {
                        String currentHash = foliageModel.stateHash();
                        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                            String syncHash = "NONE";

                            if (connection.getFoliageSync().containsKey(foliageModel.uuid)) {
                                syncHash = connection.getFoliageSync().get(foliageModel.uuid);
                            }

                            if(!currentHash.equalsIgnoreCase(syncHash)) {
                                // Does not equal emit the sync.
                                foliageModel.syncToPlayer(connection,true);
                            }
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                // Sync Entites
                for (GameEntityModel model : DedicatedServer.instance.getWorld().entites.values()) {
                    for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                        model.syncState(connection);
                    }
                }
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
