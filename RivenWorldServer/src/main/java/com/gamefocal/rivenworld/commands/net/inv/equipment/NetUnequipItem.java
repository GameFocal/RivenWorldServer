package com.gamefocal.rivenworld.commands.net.inv.equipment;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "invuneq", sources = "tcp")
public class NetUnequipItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
//        System.out.println(message.toString());
//
//        int index = Integer.parseInt(message.args[0]);
//
//        EquipmentSlot s = netConnection.getPlayer().equipmentSlots.getSlotTypeByIndex(index);
//
//        System.out.println(s.name());
//
//        netConnection.unequipTool(s);
//        netConnection.syncEquipmentSlots();

//        InventoryStack s = netConnection.getPlayer().equipmentSlots.getByIndex(index);
//        if (s != null) {
//
//            if (netConnection.getPlayer().inventory.canAdd(s)) {
//
//                netConnection.getPlayer().inventory.add(s);
//
//                EquipmentSlots slots = netConnection.getPlayer().equipmentSlots;
//                slots.setByIndex(index, null);
//
//                netConnection.syncEquipmentSlots();
////                Thread.sleep(50);
//
//                TaskService.scheduledDelayTask(() -> {
//                    netConnection.updateInventory(netConnection.getPlayer().inventory);
//                }, 1L, false);
//            }
//        }
    }
}
