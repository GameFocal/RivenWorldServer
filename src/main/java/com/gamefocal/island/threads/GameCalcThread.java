package com.gamefocal.island.threads;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.thread.AsyncThread;
import com.gamefocal.island.entites.thread.HiveAsyncThread;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.service.PlayerService;

@AsyncThread(name = "game-calc-thread")
public class GameCalcThread implements HiveAsyncThread {
    @Override
    public void run() {
        while (DedicatedServer.isRunning) {
            try {

                /*
                 * Calc player distances
                 * */
                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {

                    Location playerLoc = connection.getPlayer().location;

                    for (HiveNetConnection connection1 : DedicatedServer.get(PlayerService.class).players.values()) {
                        if (connection1.getUuid() != connection.getUuid()) {
                            Location otherLoc = connection1.getPlayer().location;

                            float dist = playerLoc.dist(otherLoc);
                            connection.updatePlayerDistance(connection1.getUuid(), dist);

                        }
                    }

                }

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
