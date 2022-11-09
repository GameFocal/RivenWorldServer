package com.gamefocal.island.commands.net;

import com.gamefocal.island.entites.net.*;

@Command(name = "voip", sources = "udp")
public class VoipCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println(message.args[0]);
    }
}
