package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.RespawnService;

@Command(name = "spawn", sources = "chat")
public class RespawnCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            if (message.args.length == 0) {
                netConnection.tpToLocation(DedicatedServer.get(RespawnService.class).randomSpawnLocation());
            } else if (message.args.length == 1) {
                HiveNetConnection c = DedicatedServer.getPlayerFromName(message.args[0]);
                if (c != null) {
                    c.tpToLocation(DedicatedServer.get(RespawnService.class).randomSpawnLocation());
                }
            }
        }
    }
}
