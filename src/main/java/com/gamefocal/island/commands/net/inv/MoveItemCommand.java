package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.service.InventoryService;

import java.util.UUID;

@Command(name = "invmv", sources = "tcp")
public class MoveItemCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // invmv|{inv}|{slot}|{amt}|{toinv}|{toslot}

        String fromInv = message.args[0];
        Integer fromSlot = Integer.valueOf(message.args[1]);
        Integer amt = Integer.valueOf(message.args[2]);
        String toInv = message.args[3];
        Integer toSlot = Integer.valueOf(message.args[4]);

        Inventory from = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(fromInv));
        Inventory to = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(toInv));

        System.out.println("FROM: " + from.getUuid() + " TO: " + to.getUuid() + " Slot: " + toSlot);

        if (from != null && to != null) {

            if (from.isOwner(netConnection) && to.isOwner(netConnection)) {



            }

        }

    }
}
