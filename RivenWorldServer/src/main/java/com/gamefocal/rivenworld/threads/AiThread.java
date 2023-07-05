package com.gamefocal.rivenworld.threads;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.service.AiService;
import io.airbrake.javabrake.Airbrake;

@AsyncThread(name = "ai-sync")
public class AiThread implements HiveAsyncThread {
    @Override
    public void run() {

        long sleepTime = 5;
        long start = 0L;
        long deltaTime = 0;

        while (true) {
            start = System.currentTimeMillis();
            try {
                // AI Tick
                DedicatedServer.get(AiService.class).processAiTick();
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
