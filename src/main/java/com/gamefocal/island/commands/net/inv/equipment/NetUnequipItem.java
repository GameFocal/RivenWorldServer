package com.gamefocal.island.commands.net.inv.equipment;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.equipment.EquipmentSlots;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.service.TaskService;

@Command(name = "invuneq", sources = "tcp")
public class NetUnequipItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println(message.toString());

        int index = Integer.parseInt(message.args[0]);

        InventoryStack s = netConnection.getPlayer().equipmentSlots.getByIndex(index);
        if (s != null) {

            if (netConnection.getPlayer().inventory.canAdd(s)) {

                netConnection.getPlayer().inventory.add(s);

                EquipmentSlots slots = netConnection.getPlayer().equipmentSlots;
                slots.setByIndex(index, null);

                netConnection.syncEquipmentSlots();
//                Thread.sleep(50);

                TaskService.scheduledDelayTask(() -> {
                    netConnection.updateInventory(netConnection.getPlayer().inventory);
                }, 1L, false);
            }
        }
    }
}
