package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.entites.net.*;

@Command(name = "inveq", sources = "tcp")
public class EquipItemCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // TODO: Equip an item from Inventory
    }
}
