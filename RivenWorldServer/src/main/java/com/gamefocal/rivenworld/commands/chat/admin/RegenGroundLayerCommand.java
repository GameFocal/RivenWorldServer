package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.ResourceService;

@Command(name = "regenground", sources = "chat,cli")
public class RegenGroundLayerCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (source == CommandSource.CONSOLE || (source == CommandSource.CHAT && netConnection.isAdmin())) {
            DedicatedServer.get(ResourceService.class).respawnGroundNodes();
            ResourceService.lastGroundLayerRespawn = System.currentTimeMillis();
        }
    }
}
