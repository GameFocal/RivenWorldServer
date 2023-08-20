package com.gamefocal.rivenworld.commands.cli;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "opsave", sources = "chat,cli")
public class OpSave extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (source == CommandSource.CHAT && !netConnection.isAdmin()) {
            return;
        }

        DedicatedServer.instance.saveAdminFile();
    }
}
