package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.game.ai.AiSpawn;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.service.AiService;

import java.util.concurrent.TimeUnit;

public class AiListener implements EventInterface {

    @EventHandler
    public void onWorldSync(ServerWorldSyncEvent event) {

        if (DedicatedServer.get(AiService.class).spawners.size() > 0) {
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - AiService.lastAiSpawnCheck) > 5) {
                AiService.lastAiSpawnCheck = System.currentTimeMillis();
                // Check here
                for (AiSpawn spawn : DedicatedServer.get(AiService.class).spawners.values()) {
                    spawn.spawn();
                }

                /*
                 * Cleanup dead animals over 5 minutes old
                 * */
                for (LivingEntity livingEntity : DedicatedServer.instance.getWorld().getEntitesOfType(LivingEntity.class)) {
                    if (!livingEntity.isAlive()) {
                        if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - livingEntity.getDeathAt()) >= 5) {
                            DedicatedServer.instance.getWorld().despawn(livingEntity.uuid);
                        }
                    }
                }

            }
        }

    }

}
