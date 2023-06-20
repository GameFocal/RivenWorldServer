package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.RespawnService;

@Command(name = "kill", sources = "chat", aliases = "lr")
public class KillCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            DedicatedServer.get(RespawnService.class).killPlayer(netConnection, null);
        }
    }
}
