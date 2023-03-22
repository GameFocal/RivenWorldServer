package com.gamefocal.rivenworld.threads;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.service.AiService;
import com.gamefocal.rivenworld.service.PeerVoteService;

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

                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
