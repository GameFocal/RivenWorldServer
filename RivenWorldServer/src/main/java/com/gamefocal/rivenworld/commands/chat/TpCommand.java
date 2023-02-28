package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "tp", sources = "chat", aliases = "tpa")
public class TpCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            if (message.cmd.equalsIgnoreCase("tp")) {
                // TP To the player

                String playerName = message.args[0];

                HiveNetConnection connection = DedicatedServer.getPlayerFromName(playerName);
                if (connection != null) {
                    netConnection.tpToLocation(connection.getPlayer().location);
                    netConnection.sendChatMessage("Sending you to the player");
                }

            } else if (message.cmd.equalsIgnoreCase("tpa")) {
                // TP the player to you
                String playerName = message.args[0];

                HiveNetConnection connection = DedicatedServer.getPlayerFromName(playerName);
                if (connection != null) {
                    connection.tpToLocation(netConnection.getPlayer().location);
                    netConnection.sendChatMessage("Pulling the player to you");
                }
            }
        }
    }
}