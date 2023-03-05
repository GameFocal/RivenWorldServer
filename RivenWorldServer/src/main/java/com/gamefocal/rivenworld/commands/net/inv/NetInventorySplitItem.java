package com.gamefocal.rivenworld.commands.net.inv;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "isp",sources = "tcp")
public class NetInventorySplitItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println(message.toString());
    }
}
