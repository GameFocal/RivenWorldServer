package com.gamefocal.island.commands.net.inv.hotbar;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.InventoryStack;

@Command(name = "hotbswap")
public class NetHotbarSwap extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println(message);
    }
}
