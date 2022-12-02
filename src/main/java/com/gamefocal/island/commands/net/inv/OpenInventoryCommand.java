package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.inv.InventoryCloseEvent;
import com.gamefocal.island.game.items.StoneHatchet;

@Command(name = "invopen", sources = "tcp")
public class OpenInventoryCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // Open the inventory.
        String inv = message.args[0];

        if (inv.equalsIgnoreCase("self")) {
            // The player inv

            // DEBUG
            if (!netConnection.getPlayer().inventory.hasOfType(StoneHatchet.class)) {
                netConnection.getPlayer().inventory.add(new StoneHatchet());
            }

            InventoryCloseEvent event = new InventoryCloseEvent(netConnection.getPlayer().inventory, netConnection).call();

            if (event.isCanceled()) {
                return;
            }

            netConnection.openInventory(netConnection.getPlayer().inventory, true);
        } else {
            // TODO: Chest and other storage containers.
        }

    }
}
