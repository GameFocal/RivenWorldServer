package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "FLY", sources = "chat")
public class FlyCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            netConnection.sendTcp("FLY|");
        }
    }
}
