package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.player.PlayerMoveEvent;
import com.gamefocal.island.game.player.PlayerBlendState;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.PlayerModel;
import com.gamefocal.island.service.NetworkService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Command(name = "plmv", sources = "udp")
public class NetPlayerMove extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println("LM1");

        PlayerModel p = netConnection.getPlayer();
        if (p != null) {

            System.out.println("PLMV");

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
            state = DedicatedServer.gson.fromJson(stateString,PlayerBlendState.class);

            netConnection.getState().blendState = state;

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
