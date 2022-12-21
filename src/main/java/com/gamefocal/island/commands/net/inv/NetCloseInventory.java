package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.inv.InventoryCloseEvent;
import com.gamefocal.island.service.InventoryService;

@Command(name = "invclose", sources = "tcp")
public class NetCloseInventory extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        InventoryCloseEvent event = new InventoryCloseEvent(netConnection.getOpenInventory(), netConnection).call();

        if (event.isCanceled()) {
            return;
        }

        if (netConnection.getOpenInventory() != null) {
            netConnection.getOpenInventory().releaseOwnership();

            // Clear the crafting queue.
            if (netConnection.getOpenInventory().canCraft()) {
                if (netConnection.getOpenInventory().getCraftingQueue().isRequireOpen()) {
                    netConnection.getOpenInventory().getCraftingQueue().clearAndReturnToSource();
                }
            }

            netConnection.closeInventory(netConnection.getOpenInventory());
        }
    }
}
