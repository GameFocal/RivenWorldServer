package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.items.StoneHatchet;
import com.gamefocal.island.game.util.InventoryUtil;

@Command(name = "invclose", sources = "tcp")
public class CloseInventoryCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        netConnection.closeInventory();
    }
}
