package com.gamefocal.rivenworld.commands.net.inv;

import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.items.generics.EquipmentItem;

@Command(name = "neqi", sources = "tcp")
public class NetEquipItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // neqi|23|BODY

//        System.out.println(message.toString());

        InventoryStack stack = netConnection.getPlayer().inventory.get(Integer.parseInt(message.args[0]));

        if (stack != null && stack.getAmount() > 0) {

            if (stack.getItem().getEquipTo() == null) {
                System.err.println("No equipment slot defined.");
                return;
            }

            Inventory fromInv = netConnection.getPlayer().inventory;
            EquipmentSlot toSlot = EquipmentSlot.valueOf(message.args[1]);
            InventoryStack eqSlot = netConnection.getPlayer().equipmentSlots.getFromSlotName(stack.getItem().getEquipTo());
            int fromSlotNumber = Integer.parseInt(message.args[0]);

            if (fromInv.get(fromSlotNumber) == null) {
                return;
            }

            InventoryItem fromItem  = fromInv.get(fromSlotNumber).getItem();

            if(!EquipmentItem.class.isAssignableFrom(fromItem.getClass())) {
                return;
            }

            EquipmentItem eqi = (EquipmentItem) fromItem;

            if(fromItem.equipTo != toSlot) {
                return;
            }

            if (eqSlot != null) {
                // Has something in this slot already
                netConnection.getPlayer().inventory.add(eqSlot);
                netConnection.getPlayer().equipmentSlots.setBySlotName(toSlot, null);
            }

            InventoryStack i = fromInv.get(fromSlotNumber);
            fromInv.clear(fromSlotNumber);
            fromInv.update();

            netConnection.getPlayer().equipmentSlots.setBySlotName(toSlot, i);

            netConnection.getPlayer().inventory.updateUIs(netConnection);

//            netConnection.updatePlayerInventory();
//            netConnection.syncEquipmentSlots();
        }
    }
}
