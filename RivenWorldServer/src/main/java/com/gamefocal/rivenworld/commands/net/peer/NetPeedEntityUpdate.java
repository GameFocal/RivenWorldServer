package com.gamefocal.rivenworld.commands.net.peer;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.OwnedEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.PeerVoteService;
import com.gamefocal.rivenworld.service.PlayerService;
import com.google.gson.JsonObject;

import java.util.UUID;

@Command(name = "npeu", sources = "udp")
public class NetPeedEntityUpdate extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // npeu|{UUID}|{LOC}

        UUID uuid = UUID.fromString(message.args[0]);
        Location location = Location.fromString(message.args[1]);

        if (DedicatedServer.get(PeerVoteService.class).ownedEntites.containsKey(uuid)) {

            if (netConnection.getPlayer().uuid.equalsIgnoreCase(DedicatedServer.get(PeerVoteService.class).ownedEntites.get(uuid).getPlayer().uuid)) {

                // Is the same Net player so allow the update
                GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(uuid);

                if (OwnedEntity.class.isAssignableFrom(e.entityData.getClass())) {

                    OwnedEntity oe = (OwnedEntity) e.entityData;

                    if (oe.onPeerUpdate(netConnection, location, new JsonObject())) {
                        DedicatedServer.instance.getWorld().getEntityFromId(uuid).location = location;
                        DedicatedServer.instance.getWorld().getEntityFromId(uuid).entityData.location = location;
                    } else {
                        DedicatedServer.instance.getWorld().getEntityFromId(uuid).version = System.currentTimeMillis();
                    }
                }
            }
        }
    }
}
