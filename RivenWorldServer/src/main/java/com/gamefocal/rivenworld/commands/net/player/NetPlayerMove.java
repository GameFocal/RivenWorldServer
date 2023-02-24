package com.gamefocal.rivenworld.commands.net.player;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.player.PlayerMoveEvent;
import com.gamefocal.rivenworld.game.player.PlayerBlendState;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.PlayerModel;

@Command(name = "plmv", sources = "udp")
public class NetPlayerMove extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

//        System.out.println(message.toString());

        PlayerModel p = netConnection.getPlayer();
        if (p != null) {
            if (!netConnection.getState().isDead) {

                String plLoc = message.args[0];

                Location l = Location.fromString(plLoc);

                PlayerMoveEvent event = new PlayerMoveEvent(netConnection, l).call();

                if (event.isCanceled()) {
                    return;
                }

                p.location = l;

                netConnection.getState().location = l;

                String stateString = message.args[1];

                PlayerBlendState state = new PlayerBlendState();
                state = DedicatedServer.gson.fromJson(stateString, PlayerBlendState.class);

                netConnection.getState().blendState = state;

                netConnection.getState().tick();

                // Get the looking at value
                if (message.args.length >= 3) {
//                JsonObject d = JsonParser.parseString(message.args[2]).getAsJsonObject();
                    netConnection.processHitData(message.args[2]);
                }

                if (message.args.length >= 4) {
                    // Has a state hash
                    netConnection.setSyncHash(message.args[3]);
                }

                if (message.args.length >= 5) {
                    Location loc = Location.fromString(message.args[4]);
                    if (loc != null) {
                        netConnection.setForwardVector(loc.toVector());
                    }
//                netConnection.setForwardVector(Location.fromString(message.args[4]).toVector());
                }

                netConnection.broadcastState();
            }
        }
    }
}