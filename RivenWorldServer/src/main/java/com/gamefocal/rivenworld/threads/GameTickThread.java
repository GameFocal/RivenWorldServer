package com.gamefocal.rivenworld.threads;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.events.game.ServerTickEvent;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.UUID;

@AsyncThread(name = "tick")
public class GameTickThread implements HiveAsyncThread {
    @Override
    public void run() {

        while (true) {
            try {
                /*
                 * Run the tasks here
                 * */
                DedicatedServer.get(TaskService.class).tick();

                new ServerTickEvent().call();

                if (DedicatedServer.instance.getWorld() != null) {
                    for (UUID uuid : DedicatedServer.instance.getWorld().tickEntites) {

                        GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(uuid);

                        if (e == null) {
                            DedicatedServer.instance.getWorld().tickEntites.remove(uuid);
                            continue;
                        }

                        e.entityData.onTick();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
