package com.gamefocal.rivenworld.commands.net.player;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "voip_dist", sources = "tcp")
public class NetChangeVoipDist extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println("Change Voice DIST to " + message.args[0]);
    }
}
