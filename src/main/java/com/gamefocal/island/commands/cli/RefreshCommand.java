package com.gamefocal.island.commands.cli;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;

@Command(name = "hive-refresh", sources = "cli,chat")
public class RefreshCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        System.out.println("Refreshing Hive HB by command...");
        DedicatedServer.licenseManager.hb();
    }
}
