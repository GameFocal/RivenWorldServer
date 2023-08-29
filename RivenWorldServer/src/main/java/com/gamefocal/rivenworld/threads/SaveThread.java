package com.gamefocal.rivenworld.threads;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.SaveService;
import io.airbrake.javabrake.Airbrake;

import java.util.concurrent.TimeUnit;

public class SaveThread implements Runnable {

    public static long lastSave = 0L;

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
                if ((!SaveService.allowNewSaves && SaveService.saveQueue.size() > 0) || (DedicatedServer.isReady && TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastSave) >= 15)) {

                    int size = SaveService.saveQueue.size();


                    for (int i = 0; i < size; i++) {
                        GameEntityModel model = SaveService.saveQueue.poll();
                        if (model != null) {
                            DataService.gameEntities.createOrUpdate(model);
                        }
                    }

                    /*
                     * Save Shops and other items every minute
                     * */
//                    if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - SaveService.lastOtherSave) >= 60) {
//                        SaveService.lastOtherSave = System.currentTimeMillis();
                    SaveService.syncNonEntities();

                    lastSave = System.currentTimeMillis();
//                    }
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
