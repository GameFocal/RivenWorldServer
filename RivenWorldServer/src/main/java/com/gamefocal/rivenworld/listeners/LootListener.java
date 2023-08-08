package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.events.game.ServerReadyEvent;
import com.gamefocal.rivenworld.game.entites.loot.LootChest;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.gamefocal.rivenworld.service.LootService;
import com.gamefocal.rivenworld.service.TaskService;

public class LootListener implements EventInterface {

    @EventHandler
    public void serverReady(ServerReadyEvent readyEvent) {

        System.out.println("Cleaning loot...");

        /*
         * Despawn all loot boxes
         * */
        for (LootChest lootChest : DedicatedServer.instance.getWorld().getEntitesOfType(LootChest.class)) {
            DedicatedServer.instance.getWorld().despawn(lootChest.uuid);
        }

        /*
         * Setup task to respawn them now :)
         * */
        TaskService.scheduleRepeatingTask(() -> {
            /*
             * Attempt to spawn in loot
             * */
            System.out.println("Respawning loot...");
            LootService lootService = DedicatedServer.get(LootService.class);
            lootService.populateWorld();
        }, 20L, TickUtil.MINUTES(15), false);
    }

}
