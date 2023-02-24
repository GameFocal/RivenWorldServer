package com.gamefocal.rivenworld.commands.net.world;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "chh", sources = "tcp")
public class NetChunkHash extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // chh|{chunkid}|{hash}
        netConnection.setChunkHash(message.args[0], message.args[1]);
//        System.out.println("Client Reported: " + message.args[0] + " = " + message.args[1]);
    }
}
