package com.gamefocal.rivenworld.commands.net.trace;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.RayService;

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
