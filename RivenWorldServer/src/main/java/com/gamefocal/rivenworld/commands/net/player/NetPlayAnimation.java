package com.gamefocal.rivenworld.commands.net.player;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "panim", sources = "tcp")
public class NetPlayAnimation extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
//        if(netConnection.getState().animation != null && netConnection.getState().animation.equalsIgnoreCase(message.args[0])) {
//            netConnection.getState().animStart++;
//        } else {
//            netConnection.getState().animation = message.args[0];
//            netConnection.getState().animStart = 0;
//        }
//
//        netConnection.getState().markDirty();
//        System.out.println("Play ANIM: " + message.toString());
    }
}
