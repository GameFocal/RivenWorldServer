package com.gamefocal.rivenworld.commands.cli;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "op", sources = "chat,cli")
public class OpCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (source == CommandSource.CHAT && !netConnection.isAdmin()) {
            return;
        }

        String id = message.args[0];

        // Check for online players first
        HiveNetConnection connection = DedicatedServer.getPlayerFromName(id);
        if (connection != null) {
            DedicatedServer.admins.push(connection.getHiveId());
            return;
        }

        DedicatedServer.admins.push(id);
    }
}
