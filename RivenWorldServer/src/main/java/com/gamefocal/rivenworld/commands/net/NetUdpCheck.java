package com.gamefocal.rivenworld.commands.net;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "nudpc", sources = "udp")
public class NetUdpCheck extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
//        System.out.println("Got UDP Reply");
        netConnection.setNetworkMode(NetworkMode.TCP_UDP);
    }
}
