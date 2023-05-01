package com.gamefocal.rivenworld.threads;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.service.AiService;
import com.gamefocal.rivenworld.service.PeerVoteService;
import io.airbrake.javabrake.Airbrake;

@AsyncThread(name = "ai-sync")
public class AiThread implements HiveAsyncThread {
    @Override
    public void run() {
        while (true) {
            try {
                // AI Tick
                DedicatedServer.get(AiService.class).processAiTick();
            } catch (Exception e) {
                e.printStackTrace();
                Airbrake.report(e);
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.yield();
            }
        }
    }
}
