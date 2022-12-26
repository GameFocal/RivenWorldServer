package com.gamefocal.island.commands.net.inv.equipment;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.equipment.EquipmentSlot;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.tasks.HiveTaskSequence;
import com.gamefocal.island.service.TaskService;

@Command(name = "inveq", sources = "tcp")
public class NetEquipItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // inveqp|{slotid}

        Integer slot = Integer.valueOf(message.args[0]);

        if (netConnection.getPlayer().inventory.isEmpty(slot)) {
            return;
        }

        netConnection.equipFromInventory(slot);

//        InventoryStack stack = netConnection.getPlayer().inventory.get(slot);
//        if (stack != null) {
//
//            HiveTaskSequence sequence = new HiveTaskSequence(false);
//
//            // See if something is already equpied
//            EquipmentSlot toSlot = stack.getItem().getEquipTo();
//            InventoryStack currentStack = netConnection.getPlayer().equipmentSlots.getItemBySlot(toSlot);
//            if (currentStack != null) {
//                System.out.println("Something is already here");
//                // Something is already equipped... We need to remove that.
//                netConnection.getPlayer().inventory.add(currentStack);
//                netConnection.getPlayer().equipmentSlots.setBySlot(toSlot, null);
//                sequence.exec(netConnection::syncEquipmentSlots);
//                sequence.await(5L);
//                sequence.exec(() -> {
//                    netConnection.updateInventory(netConnection.getPlayer().inventory);
//                });
//                sequence.await(5L);
//            }
//
//            if (stack.equip(netConnection.getPlayer())) {
//                // It has been equipped
//                netConnection.getPlayer().inventory.clear(slot);
//            }
//
//            sequence.exec(() -> {
//                netConnection.updateInventory(netConnection.getPlayer().inventory);
//            });
////                Thread.sleep(50);
//            sequence.await(1L);
//            sequence.exec(netConnection::syncEquipmentSlots);
//
//            TaskService.scheduleTaskSequence(sequence);
//        }
    }
}
