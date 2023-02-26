package com.gamefocal.rivenworld.commands.cli;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "hive-refresh", sources = "cli,chat")
public class RefreshCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (source == CommandSource.CONSOLE || (source == CommandSource.CHAT && netConnection.isAdmin())) {
            System.out.println("Refreshing Hive HB by command...");
            DedicatedServer.licenseManager.hb();
        }
    }
}
