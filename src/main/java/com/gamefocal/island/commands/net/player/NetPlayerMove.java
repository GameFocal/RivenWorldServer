package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.player.PlayerMoveEvent;
import com.gamefocal.island.game.player.PlayerBlendState;
import com.gamefocal.island.game.player.PlayerState;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.PlayerModel;
import com.gamefocal.island.service.PlayerService;

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

            String stateString = new String(Base64.getDecoder().decode(message.args[1].getBytes(StandardCharsets.UTF_8)));

//            System.out.println(stateString);

            PlayerBlendState state = new PlayerBlendState();
            state = DedicatedServer.gson.fromJson(stateString, PlayerBlendState.class);

            netConnection.getState().blendState = state;

            netConnection.getState().tick();
            String stateBlob = Base64.getEncoder().encodeToString(DedicatedServer.gson.toJson(netConnection.getState(), PlayerState.class).getBytes(StandardCharsets.UTF_8));

            for (HiveNetConnection peer : DedicatedServer.get(PlayerService.class).players.values()) {
                if (peer.getUuid() != netConnection.getUuid()) {
                    peer.sendUdp("ps|" + netConnection.getUuid().toString() + "|" + netConnection.getVoiceId() + "|" + stateBlob);
                }
            }

//            HiveNetMessage m = new HiveNetMessage();
//            message.cmd = "plmv";
//            message.args = new String[]{event.getConnection().getUuid().toString(), String.valueOf(event.getConnection().getVoiceId()), event.getLocation().toString()};
//            DedicatedServer.get(NetworkService.class).broadcastUdp(message, event.getConnection().getUuid());

//            HiveNetMessage message1 = new HiveNetMessage();
//            message1.cmd = "sync";
//            message1.args = new String[]{event.getConnection().getPlayer().location.toString()};
//            event.getConnection().sendUdp(message1.toString());
        }
    }
}
