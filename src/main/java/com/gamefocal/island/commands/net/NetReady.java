package com.gamefocal.island.commands.net;

import com.gamefocal.island.entites.net.CommandSource;
import com.gamefocal.island.entites.net.HiveCommand;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;

public class NetReady extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println("Got client ready... sending init.");
        netConnection.sendTcp("init");
    }
}
