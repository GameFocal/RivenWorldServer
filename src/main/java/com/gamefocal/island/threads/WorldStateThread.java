package com.gamefocal.island.threads;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.thread.AsyncThread;
import com.gamefocal.island.entites.thread.HiveAsyncThread;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.PlayerService;

@AsyncThread(name = "world-state")
public class WorldStateThread implements HiveAsyncThread {
    @Override
    public void run() {
        while (DedicatedServer.isRunning) {
            if (DedicatedServer.instance.getWorld() != null) {
                for (GameEntityModel model : DedicatedServer.instance.getWorld().entites.values()) {
                    for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                        model.syncState(connection);
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
