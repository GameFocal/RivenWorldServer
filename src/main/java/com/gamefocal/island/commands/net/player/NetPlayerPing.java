package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.entites.net.*;

@Command(name = "ping",sources = "tcp")
public class NetPlayerPing extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println(message.toString());
    }
}
