package com.gamefocal.island.commands.net;

import com.gamefocal.island.entites.net.*;

@Command(name = "ready",sources = "tcp")
public class NetReady extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println("Client Ready: Sending INIT");
        netConnection.sendTcp("init");
    }
}
