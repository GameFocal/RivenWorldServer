package com.gamefocal.island.commands.net.resource;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.service.RayService;
import com.gamefocal.island.service.ResourceService;

import java.util.UUID;

@Command(name = "nrayr", sources = "udp")
public class NetRayHitResult extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        String id = message.args[0];
        Location loc = Location.fromString(message.args[1]);

        DedicatedServer.get(RayService.class).processRequestReply(netConnection,id,loc);

//        DedicatedServer.get(ResourceService.class).processSpawnRayReply(UUID.fromString(id), loc);
    }
}
