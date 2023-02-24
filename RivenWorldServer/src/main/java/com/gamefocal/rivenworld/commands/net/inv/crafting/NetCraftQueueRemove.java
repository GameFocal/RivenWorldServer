package com.gamefocal.rivenworld.commands.net.inv.crafting;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.service.InventoryService;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.UUID;

@Command(name = "invcci",sources = "tcp")
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
