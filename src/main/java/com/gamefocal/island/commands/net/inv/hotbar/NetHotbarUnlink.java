package com.gamefocal.island.commands.net.inv.hotbar;

import com.gamefocal.island.entites.net.*;

@Command(name = "hotbunlink")
public class NetHotbarUnlink extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println(message);
    }
}
