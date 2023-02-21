package com.gamefocal.island.commands.net;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.service.NetworkService;

@Command(name = "nudpc", sources = "udp")
public class NetUdpCheck extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        DedicatedServer.get(NetworkService.class).processUdpReply(netConnection);
    }
}
