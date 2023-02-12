package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.player.PlayerMoveEvent;
import com.gamefocal.island.game.player.PlayerBlendState;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.PlayerModel;
import com.gamefocal.island.service.NetworkService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Command(name = "plmv", sources = "udp")
public class NetPlayerMove extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        PlayerModel p = netConnection.getPlayer();
        if (p != null) {

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
                JsonObject d = JsonParser.parseString(message.args[2]).getAsJsonObject();
                netConnection.processHitData(d);
            }

            if (message.args.length >= 4) {
                // Has a state hash
                netConnection.setSyncHash(message.args[3]);
            }

            DedicatedServer.get(NetworkService.class).broadcastUdp(netConnection.getState().getNetPacket(), netConnection.getUuid());
        }
    }
}
