package com.gamefocal.island.commands.net.inv.consume;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.service.InventoryService;

import java.util.UUID;

@Command(name = "noof",sources = "tcp")
public class NetTurnOnOff extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        Inventory inventory = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(message.args[0]));

        if(inventory != null) {
            System.out.println("INV");

            // Find the entity and see if it is on or off

        }

    }
}
