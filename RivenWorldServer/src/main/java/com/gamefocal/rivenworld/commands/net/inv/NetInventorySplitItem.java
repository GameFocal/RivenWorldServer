package com.gamefocal.rivenworld.commands.net.inv;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.service.InventoryService;

import java.util.UUID;

@Command(name = "isp", sources = "tcp")
public class NetInventorySplitItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        if(InventoryService.blockedSlots.contains(netConnection.getUuid())) {
            synchronized (InventoryService.blockedSlots) {
                InventoryService.blockedSlots.remove(netConnection.getUuid());
            }
            return;
        }

        Inventory from = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(message.args[0]));
        Inventory to = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(message.args[1]));

        Integer fromSlot = Integer.parseInt(message.args[2]);
        Integer toSlot = Integer.parseInt(message.args[3]);

        if (from != null && to != null) {
            from.transferToInventory(to, fromSlot, toSlot, true);
//            netConnection.updatePlayerInventory();

            if (from.getUuid() == to.getUuid()) {
                // Same only update once
                from.updateUIs(netConnection);
            } else {
                from.updateUIs(netConnection);
                to.updateUIs(netConnection);
            }

        }
    }
}
