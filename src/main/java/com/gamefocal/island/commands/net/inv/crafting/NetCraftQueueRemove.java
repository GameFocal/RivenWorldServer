package com.gamefocal.island.commands.net.inv.crafting;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.service.InventoryService;
import com.gamefocal.island.service.TaskService;

import java.util.UUID;

@Command(name = "invcci")
public class NetCraftQueueRemove extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        Inventory inventory = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(message.args[0]));

        if (inventory.canCraft()) {
            // If a crafting inventory
            if (inventory.isOwner(netConnection)) {
                // Is owned by the current player

                int jobIndex = Integer.parseInt(message.args[1]);

                inventory.getCraftingQueue().cancelJobByIndex(jobIndex);

                TaskService.scheduledDelayTask(() -> {
                    netConnection.updateInventory(inventory);
                }, 5L, false);
            }
        }

    }
}
