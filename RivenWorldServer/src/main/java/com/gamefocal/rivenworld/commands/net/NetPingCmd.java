package com.gamefocal.rivenworld.commands.net;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "ping",sources = "tcp")
public class NetPingCmd extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

    }
}
