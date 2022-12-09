package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.service.InventoryService;

import java.util.UUID;

@Command(name = "inveq", sources = "tcp")
public class NetEquipItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // inveqp|{slotid}

        Integer slot = Integer.valueOf(message.args[0]);

        if (netConnection.getPlayer().inventory.isEmpty(slot)) {
            return;
        }

        InventoryStack stack = netConnection.getPlayer().inventory.get(slot);
        if (stack != null) {

            if (stack.equip(netConnection.getPlayer())) {
                // It has been equipped
                netConnection.getPlayer().inventory.clear(slot);

                netConnection.updateInventory(netConnection.getPlayer().inventory);
                Thread.sleep(75);
                netConnection.syncEquipmentSlots();
            }
        }
    }
}
