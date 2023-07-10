package com.gamefocal.rivenworld.threads;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.*;
import io.airbrake.javabrake.Airbrake;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@AsyncThread(name = "world-save")
public class SaveThread implements HiveAsyncThread {
    @Override
    public void run() {

        long sleepTime = 5;
        long start = 0L;
        long deltaTime = 0;

        while (true) {
            start = System.currentTimeMillis();
            try {

                /*
                 * Poll entities
                 * */
                if (DedicatedServer.isReady) {
                    while (SaveService.saveQueue.size() > 0 && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start) <= 5) {
                        GameEntityModel model = SaveService.saveQueue.poll();
                        if (model != null) {
                            DataService.gameEntities.createOrUpdate(model);
                        }
                    }

                    /*
                     * Save Shops and other items every minute
                     * */
                    if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - SaveService.lastOtherSave) >= 60) {
                        SaveService.lastOtherSave = System.currentTimeMillis();
                        SaveService.syncNonEntities();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Airbrake.report(e);
            }
            deltaTime = System.currentTimeMillis() - start;

            if (deltaTime < 25) {
                sleepTime = (25 - deltaTime);
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.yield();
            }
        }
    }
}
