package com.gamefocal.island.commands.net.util;

import com.gamefocal.island.entites.net.*;

@Command(name = "fsync",sources = "udp")
public class RecvFoliageSyncCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println(message.toString());
    }
}
