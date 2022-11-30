package com.gamefocal.island.commands.net;

import com.gamefocal.island.entites.net.*;

@Command(name = "invopen", sources = "tcp")
public class OpenInventoryCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println("[INV]: " + message.toString());

        // Open the inventory.
        String inv = message.args[0];

        if (inv.equalsIgnoreCase("self")) {
            // The player inv
            netConnection.openInventory(netConnection.getPlayer().inventory,true);
        } else {
            // TODO: Chest and other storage containers.
        }

    }
}
