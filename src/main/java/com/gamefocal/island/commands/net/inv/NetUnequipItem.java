package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.EquipmentSlots;

@Command(name = "invuneq", sources = "tcp")
public class NetUnequipItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println(message.toString());

        EquipmentSlots slots = new EquipmentSlots();

    }
}
