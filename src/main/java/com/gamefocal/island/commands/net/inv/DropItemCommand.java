package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.entites.net.*;

@Command(name = "invdrop", sources = "tcp")
public class DropItemCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // TODO: Drop an item out of inventory
    }
}
