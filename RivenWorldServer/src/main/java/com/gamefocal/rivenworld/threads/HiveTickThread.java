package com.gamefocal.rivenworld.threads;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import io.airbrake.javabrake.Airbrake;

import java.util.concurrent.TimeUnit;

@AsyncThread(name = "hive-tick-thread")
public class HiveTickThread implements HiveAsyncThread {

    private static long nextHb = 0L;

    @Override
    public void run() {
        while (true) {

            if (nextHb > 0 && nextHb > System.currentTimeMillis()) {
                Thread.yield();
                continue;
            }

            if(!DedicatedServer.isReady) {
                Thread.yield();
                continue;
            }

            try {
                DedicatedServer.licenseManager.hb();

                nextHb = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30);

            } catch (Exception e) {
                Airbrake.report(e);
                e.printStackTrace();
            }

            Thread.yield();
        }
    }
}
