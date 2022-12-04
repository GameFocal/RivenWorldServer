package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.entites.net.*;

@Command(name = "voip_dist", sources = "tcp")
public class ChangeVoipDistCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println("Change Voice DIST to " + message.args[0]);
    }
}
