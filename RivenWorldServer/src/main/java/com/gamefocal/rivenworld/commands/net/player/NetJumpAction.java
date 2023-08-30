package com.gamefocal.rivenworld.commands.net.player;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "JUMP", sources = "tcp")
public class NetJumpAction extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.getPlayer().playerStats.energy > 5) {
            netConnection.getPlayer().playerStats.energy -= 5;
        }
    }
}
