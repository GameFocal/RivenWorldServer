package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.events.game.ServerReadyEvent;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.game.ai.AiSpawn;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.service.AiService;

import java.util.concurrent.TimeUnit;

public class AiListener implements EventInterface {

    @EventHandler
    public void onServerReadyEvent(ServerReadyEvent event) {

        System.out.println("Generating AI Blocks...");

        AiService aiService = DedicatedServer.get(AiService.class);

        // Monistary
        aiService.blockCellsInArea("111874.73,70977.805,13798.965,0.0,0.0,44.311882", "115679.96,73680.18,13846.187,0.0,0.0,-120.18956");

        // Town Church
        aiService.blockCellsInArea("151558.1,91070.9,3584.176,0.0,0.0,-11.227478", "153977.73,88574.78,3397.317,0.0,0.0,155.58607");

        // Town Windmill
        aiService.blockCellsInArea("151055.73,82963.836,4276.4224,0.0,0.0,34.194824", "151878.92,83775.98,4276.4224,0.0,0.0,-142.51082");

        // Town Blacksmith
        aiService.blockCellsInArea("151574.95,82473.234,4276.283,0.0,0.0,-126.11322", "150768.05,80662.72,4276.283,0.0,0.0,41.510834");

        // General Store
        aiService.blockCellsInArea("152056.53,80381.33,4276.283,0.0,0.0,-38.91916", "153574.28,79164.53,4276.283,0.0,0.0,133.85257");

        // Misc
        aiService.blockCellsInArea("153855.27,80757.07,4276.283,0.0,0.0,7.490543", "155575.67,81066.81,4273.0923,0.0,0.0,-179.28065");
        aiService.blockCellsInArea("154459.33,83372.016,4276.0884,0.0,0.0,-65.75372", "155767.4,82058.945,4276.2627,0.0,0.0,133.53633");
        aiService.blockCellsInArea("156479.02,89154.52,3598.2668,0.0,0.0,123.54739", "155266.77,91279.15,3572.4314,0.0,0.0,-66.333374");
        aiService.blockCellsInArea("32674.74,134470.1,12006.78,0.0,0.0,-104.68651", "33472.402,132855.44,12006.308,0.0,0.0,99.98795");
        aiService.blockCellsInArea("35467.902,133482.56,12134.79,0.0,0.0,-31.855042", "36776.406,132164.47,12134.79,0.0,0.0,154.60771");
        aiService.blockCellsInArea("37272.25,134859.78,12153.051,0.0,0.0,31.237928", "38779.223,136082.23,12162.215,0.0,0.0,-152.73193");
        aiService.blockCellsInArea("35969.426,138184.23,12162.3,0.0,0.0,-95.41312", "35920.355,138252.12,12160.856,0.0,0.0,-91.36182");

        // Throne
        aiService.blockCellsInArea("72473.03,114062.555,25269.783,0.0,0.0,83.28983", "72464.84,116475.766,25269.783,0.0,0.0,-86.797485");
    }

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
