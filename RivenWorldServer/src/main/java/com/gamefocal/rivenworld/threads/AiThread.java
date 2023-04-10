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
                // Check for ownerships
                DedicatedServer.get(PeerVoteService.class).processOwnerships();

                // AI Tick
                DedicatedServer.get(AiService.class).processAiTick();
            } catch (Exception e) {
                e.printStackTrace();
                Airbrake.report(e);
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.yield();
                e.printStackTrace();
            }
        }
    }
}
