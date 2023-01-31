package com.gamefocal.island.commands.chat;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.util.Location;
import org.apache.commons.lang3.tuple.Pair;

@Command(name = "debug",sources = "chat")
public class DebugCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (message.args[0].equalsIgnoreCase("hm")) {
            // Draw a heightmap
            float h = DedicatedServer.instance.getWorld().generator.getHeightmap().getHeightFromLocation(new Location(netConnection.getPlayer().location.getX(), netConnection.getPlayer().location.getY(), 0));

            Location nLoc = netConnection.getPlayer().location.cpy();
            nLoc.setZ((float) (h*.65));

            System.out.println("Player LOC: " + netConnection.getPlayer().location);
            System.out.println("HM Loc: " + nLoc);

            netConnection.sendTcp("dbug-dot|" + nLoc.toString());
        }
    }
}
