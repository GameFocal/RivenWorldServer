package com.gamefocal.rivenworld.commands.net.player;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "ping",sources = "tcp")
public class NetPlayerPing extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
//        System.out.println(message.toString());
    }
}
