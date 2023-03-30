package com.gamefocal.rivenworld.commands.net.inv;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.service.InventoryService;

import java.util.UUID;

@Command(name = "nueqi", sources = "tcp")
public class NetUnEquipItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        EquipmentSlot fromSlot = EquipmentSlot.valueOf(message.args[0]);
        Inventory toInv = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(message.args[1]));
        int toSlot = Integer.parseInt(message.args[2]);

        if (toInv != null) {

            InventoryStack from = netConnection.getPlayer().equipmentSlots.getFromSlotName(fromSlot);
            if (from != null) {
                if (toInv.get(toSlot) == null || toInv.get(toSlot).getAmount() <= 0) {
                    // Is a empty slot
                    toInv.set(toSlot, from);
                    netConnection.getPlayer().equipmentSlots.setBySlotName(fromSlot, null);
                } else {
                    // Has an existing item
                    if (toInv.hasEmptySlot()) {
                        toInv.add(from);
                        netConnection.getPlayer().equipmentSlots.setBySlotName(fromSlot, null);
                    }
                }
            }

        }

        netConnection.syncEquipmentSlots();
        netConnection.getPlayer().inventory.updateUIs(netConnection);
    }
}
