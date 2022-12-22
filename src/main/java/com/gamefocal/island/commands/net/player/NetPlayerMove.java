package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.player.PlayerMoveEvent;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.PlayerModel;
import com.gamefocal.island.service.NetworkService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            HiveNetMessage m = new HiveNetMessage();
            message.cmd = "plmv";
            message.args = new String[]{event.getConnection().getUuid().toString(), String.valueOf(event.getConnection().getVoiceId()), event.getLocation().toString()};
            DedicatedServer.get(NetworkService.class).broadcastUdp(message, event.getConnection().getUuid());

//            HiveNetMessage message1 = new HiveNetMessage();
//            message1.cmd = "sync";
//            message1.args = new String[]{event.getConnection().getPlayer().location.toString()};
//            event.getConnection().sendUdp(message1.toString());
        }
    }
}
