package com.gamefocal.rivenworld.commands.net.player;

import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;

@Command(name = "qeq", sources = "tcp")
public class NetQuickHotbarSelect extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

//        System.out.println(message.toString());

        netConnection.getPlayer().inventory.setHotBarSelection(Integer.parseInt(message.args[0]));

        // Attempt to equip this...
        if (!netConnection.getPlayer().inventory.equipFromSlot(netConnection, Integer.parseInt(message.args[0]), EquipmentSlot.PRIMARY)) {
            netConnection.getPlayer().equipmentSlots.inHand = null;
        }

        netConnection.updatePlayerInventory();
        netConnection.syncEquipmentSlots();

        System.out.println(netConnection.getPlayer().equipmentSlots.toJson().toString());

    }
}
