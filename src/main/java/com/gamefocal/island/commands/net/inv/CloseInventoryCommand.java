package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.inv.InventoryCloseEvent;

@Command(name = "invclose", sources = "tcp")
public class CloseInventoryCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        InventoryCloseEvent event = new InventoryCloseEvent(netConnection.getOpenInventory(), netConnection).call();

        if (event.isCanceled()) {
            return;
        }

        netConnection.closeInventory();
    }
}
