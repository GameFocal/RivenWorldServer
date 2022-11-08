package com.gamefocal.island.commands.net;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.PlayerMoveEvent;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.PlayerModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Command(name = "plmv", sources = "udp")
public class PlMvCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        PlayerModel p = netConnection.getPlayer();
        if (p != null) {
            String plLoc = message.args[0];
            Pattern pattern = Pattern.compile("X\\=(.*?)Y\\=(.*?)Z\\=(.*)");
            Matcher matcher = pattern.matcher(plLoc);

            if (matcher.find()) {
                Location l = new Location(
                        Float.parseFloat(matcher.group(1)),
                        Float.parseFloat(matcher.group(2)),
                        Float.parseFloat(matcher.group(3))
                );

                p.location = l;

                new PlayerMoveEvent(netConnection, l).call();
            }
        }
    }
}
