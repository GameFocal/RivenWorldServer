package com.gamefocal.rivenworld.commands.net.inv;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "imv",sources = "tcp")
public class NetInventoryMoveItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println(message.toString());
    }
}
