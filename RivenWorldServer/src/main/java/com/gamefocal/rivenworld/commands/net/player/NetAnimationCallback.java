package com.gamefocal.rivenworld.commands.net.player;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "animcb", sources = "tcp")
public class NetAnimationCallback extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.getAnimationCallback() != null) {
            netConnection.getAnimationCallback().onRun(netConnection, message.args);
            netConnection.setAnimationCallback(null);
        }
    }
}
