package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.entites.net.*;

@Command(name = "invclose", sources = "tcp")
public class NetCloseInventory extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        netConnection.getOpenUI().close(netConnection);
    }
}
