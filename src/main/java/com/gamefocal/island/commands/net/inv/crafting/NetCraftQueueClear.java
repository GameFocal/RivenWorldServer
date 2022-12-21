package com.gamefocal.island.commands.net.inv.crafting;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.CraftingRecipe;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryCraftingInterface;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.crafting.CraftingJob;
import com.gamefocal.island.game.tasks.seqence.ExecSequenceAction;
import com.gamefocal.island.service.InventoryService;
import com.gamefocal.island.service.TaskService;

import java.util.Map;
import java.util.UUID;

@Command(name = "invcca")
public class NetCraftQueueClear extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        Inventory inventory = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(message.args[0]));

        if (inventory.canCraft()) {
            // If a crafting inventory
            if (inventory.isOwner(netConnection)) {
                // Is owned by the current player

                inventory.getCraftingQueue().clearAndReturnToSource();

                TaskService.scheduledDelayTask(() -> {
                    netConnection.updateInventory(inventory);
                }, 5L, false);
            }
        }

    }
}
