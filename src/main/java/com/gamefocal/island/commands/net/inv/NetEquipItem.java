package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.service.InventoryService;

import java.util.UUID;

@Command(name = "inveq", sources = "tcp")
public class NetEquipItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // inveqp|{slotid}

        Integer slot = Integer.valueOf(message.args[0]);

        Inventory inv = netConnection.getPlayer().inventory;

        if (inv.isEmpty(slot)) {
            return;
        }

        // TODO: Finish this.

    }
}
