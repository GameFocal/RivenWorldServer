package com.gamefocal.island.threads;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.thread.AsyncThread;
import com.gamefocal.island.entites.thread.HiveAsyncThread;
import com.gamefocal.island.service.PlayerService;
import com.gamefocal.island.service.TaskService;

@AsyncThread(name = "player-states")
public class PlayStateThread implements HiveAsyncThread {
    @Override
    public void run() {

        try {
            while (DedicatedServer.isRunning) {

                /*
                 * Run the tasks here
                 * */
                DedicatedServer.get(TaskService.class).tick();

//                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//
//                    /*
//                     * Run sync update here
//                     * */
//
//
//                }

                Thread.sleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
