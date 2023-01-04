package com.gamefocal.island.threads;

import com.badlogic.gdx.utils.compression.lzma.Base;
import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.thread.AsyncThread;
import com.gamefocal.island.entites.thread.HiveAsyncThread;
import com.gamefocal.island.game.player.PlayerState;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.PlayerService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@AsyncThread(name = "world-state")
public class WorldStateThread implements HiveAsyncThread {
    @Override
    public void run() {
        while (DedicatedServer.isRunning) {
            if (DedicatedServer.instance.getWorld() != null) {
                // Sync Entites
                for (GameEntityModel model : DedicatedServer.instance.getWorld().entites.values()) {
                    for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                        model.syncState(connection);
                    }
                }

                // Sync Player States
                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                    for (Map.Entry<UUID, Float> m : connection.getPlayerDistances().entrySet()) {
                        if (m.getKey() != connection.getUuid()) {
                            if (m.getValue() <= 50000) {
                                // Is within a range to sync the player
                                HiveNetConnection peer = DedicatedServer.get(PlayerService.class).players.get(m.getKey());

                                peer.getState().tick();

                                String oldStateHash = null;
                                if (connection.getSubStates().containsKey(m.getKey())) {
                                    // Already subbed
                                    oldStateHash = connection.getSubStates().get(m.getKey());
                                }

                                if (oldStateHash == null || !oldStateHash.equalsIgnoreCase(peer.getState().hash)) {
                                    // Has a old hash so needs a update
                                    connection.sendUdp("ps|" + peer.getUuid().toString() + "|" + peer.getVoiceId() + "|" + Base64.getEncoder().encodeToString(DedicatedServer.gson.toJson(peer.getState(), PlayerState.class).getBytes(StandardCharsets.UTF_8)));
                                    connection.getSubStates().put(m.getKey(), peer.getState().hash);
                                }
                            }
                        }
                    }
                }
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
