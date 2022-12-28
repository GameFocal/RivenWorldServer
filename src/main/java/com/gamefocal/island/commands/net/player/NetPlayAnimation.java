package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.entites.net.*;

@Command(name = "panim", sources = "tcp")
public class NetPlayAnimation extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println("Play ANIM: " + message.toString());
    }
}
