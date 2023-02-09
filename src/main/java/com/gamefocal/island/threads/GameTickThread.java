package com.gamefocal.island.threads;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.thread.AsyncThread;
import com.gamefocal.island.entites.thread.HiveAsyncThread;
import com.gamefocal.island.events.game.ServerTickEvent;
import com.gamefocal.island.service.PlayerService;
import com.gamefocal.island.service.TaskService;

@AsyncThread(name = "tick")
public class GameTickThread implements HiveAsyncThread {
    @Override
    public void run() {

        try {
            while (true) {
                /*
                 * Run the tasks here
                 * */
                DedicatedServer.get(TaskService.class).tick();

                new ServerTickEvent().call();

                Thread.sleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
