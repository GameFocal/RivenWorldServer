package com.gamefocal.island.threads;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.thread.AsyncThread;
import com.gamefocal.island.entites.thread.HiveAsyncThread;
import com.gamefocal.island.game.player.PlayerState;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.service.PlayerService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@AsyncThread(name = "player-state")
public class PlayerStateThread implements HiveAsyncThread {
    @Override
    public void run() {
        while (true) {
            try {
                /*
                 * Calc player distances
                 * */
                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {

                    Location playerLoc = connection.getPlayer().location;

                    connection.getState().tick();

                    String stateBlob = Base64.getEncoder().encodeToString(DedicatedServer.gson.toJson(connection.getState(), PlayerState.class).getBytes(StandardCharsets.UTF_8));

                    for (HiveNetConnection connection1 : DedicatedServer.get(PlayerService.class).players.values()) {
                        if (connection1.getUuid() != connection.getUuid()) {
                            Location otherLoc = connection1.getPlayer().location;

                            float dist = playerLoc.dist(otherLoc);
                            connection.updatePlayerDistance(connection1.getUuid(), dist);

                            // SYNC
                            if (dist <= 50000) {
                                // Is within a range to sync the player
//                            connection1.getState().tick();

                                String oldStateHash = null;
                                if (connection1.getSubStates().containsKey(connection.getUuid())) {
                                    // Already subbed
                                    oldStateHash = connection1.getSubStates().get(connection.getUuid());
                                }

                                if (oldStateHash == null || !oldStateHash.equalsIgnoreCase(connection.getState().hash)) {
                                    // Has a old hash so needs a update
                                    connection1.sendTcp("ps|" + connection.getUuid().toString() + "|" + connection.getVoiceId() + "|" + stateBlob);
                                    connection1.getSubStates().put(connection.getUuid(), connection.getState().hash);
                                }
                            }
                            // END SYNC

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
