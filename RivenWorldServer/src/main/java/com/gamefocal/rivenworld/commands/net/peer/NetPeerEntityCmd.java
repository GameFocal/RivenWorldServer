package com.gamefocal.rivenworld.commands.net.peer;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.entites.generics.OwnedEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.PeerVoteService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.UUID;

@Command(name = "npec", sources = "tcp")
public class NetPeerEntityCmd extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // npec|{UUID}|{data}

        UUID uuid = UUID.fromString(message.args[0]);

        if (DedicatedServer.get(PeerVoteService.class).ownedEntites.containsKey(uuid)) {

            if (netConnection.getPlayer().uuid.equalsIgnoreCase(DedicatedServer.get(PeerVoteService.class).ownedEntites.get(uuid).getPlayer().uuid)) {

                // Is the same Net player so allow the update
                GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(uuid);

                if (OwnedEntity.class.isAssignableFrom(e.entityData.getClass())) {

                    OwnedEntity oe = (OwnedEntity) e;

                    JsonObject d = new JsonObject();
                    if (message.args.length >= 3) {
                        d = JsonParser.parseString(message.args[2]).getAsJsonObject();
                    }

                    oe.onPeerCmd(netConnection, message.args[1], d);
                }
            }
        }
    }
}
