package com.gamefocal.rivenworld.commands.net.inv;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "invclose", sources = "tcp")
public class NetCloseInventory extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.getOpenUI() != null) {
            netConnection.getOpenUI().close(netConnection);
        }
    }
}
