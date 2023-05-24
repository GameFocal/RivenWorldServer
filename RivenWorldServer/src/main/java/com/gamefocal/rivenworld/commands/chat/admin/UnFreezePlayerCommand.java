package com.gamefocal.rivenworld.commands.chat.admin;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "unfreeze", sources = "chat")
public class UnFreezePlayerCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        boolean can = true;
        if (source == CommandSource.CHAT) {
            can = netConnection.isAdmin();
        }

        if (can) {

            String toKick = message.args[0];

            HiveNetConnection connection = DedicatedServer.getPlayerFromName(toKick);
            if (connection != null) {
                connection.enableMovment();
                connection.clearProgressBar();
            }
        }
    }
}
