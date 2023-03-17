package com.gamefocal.rivenworld.commands.net.ai;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "nais", sources = "udp")
public class NetAiSync extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println(message);
    }
}
