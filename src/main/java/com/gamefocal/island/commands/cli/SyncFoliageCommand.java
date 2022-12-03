package com.gamefocal.island.commands.cli;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.models.GameMetaModel;
import com.gamefocal.island.service.NetworkService;

@Command(name = "syncfoliage", sources = "cli")
public class SyncFoliageCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // Sync Foliage from the players

        HiveNetMessage message1 = new HiveNetMessage();
        message1.cmd = "worldsync";

        DedicatedServer.get(NetworkService.class).broadcast(message1, null);

        System.out.println("Syncing Foliage from Game to Server... Fly around to complete this.");
    }
}
