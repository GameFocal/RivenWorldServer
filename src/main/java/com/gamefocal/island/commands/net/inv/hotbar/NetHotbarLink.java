package com.gamefocal.island.commands.net.inv.hotbar;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.InventoryStack;

@Command(name = "hotblink")
public class NetHotbarLink extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println(message);

        int invIndex = Integer.parseInt(message.args[0]);
        int slotIndex = Integer.parseInt(message.args[1]);
        boolean isEquipment = Boolean.parseBoolean(message.args[2]);
        boolean isSwap = Boolean.parseBoolean(message.args[3]);

        if (isSwap) {
            System.out.println(message);
        } else {
            InventoryStack stack = null;

            if (isEquipment) {
                stack = netConnection.getPlayer().equipmentSlots.getByIndex(invIndex);
            } else {
                stack = netConnection.getPlayer().inventory.get(invIndex);
            }

            if (stack != null) {
                // Has a stack here
                netConnection.getPlayer().hotbar.linkToSlot(stack, slotIndex);
                netConnection.syncHotbar();
            }
        }
    }
}
