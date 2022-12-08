package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.entites.net.*;

@Command(name = "invuneq", sources = "tcp")
public class NetUnequipItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // TODO: Un-Equip an item from Inventory
    }
}
