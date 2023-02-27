package com.gamefocal.rivenworld.threads;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import io.airbrake.javabrake.Airbrake;

@AsyncThread(name = "hive-tick-thread")
public class HiveTickThread implements HiveAsyncThread {
    @Override
    public void run() {
        while (true) {
            try {
                DedicatedServer.licenseManager.hb();
                Thread.sleep(5000);
            } catch (Exception e) {
                Airbrake.report(e);
                e.printStackTrace();
            }
        }
    }
}
