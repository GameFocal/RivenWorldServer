package com.gamefocal.island.commands.net.building;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.building.BuildPreviewLocationUpdateEvent;
import com.gamefocal.island.game.util.Location;

@Command(name = "nprev", sources = "udp")
public class NetPreviewLocation extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        Location location = Location.fromString(message.args[0]);
        netConnection.setBuildPreviewLocation(location);

        BuildPreviewLocationUpdateEvent event = new BuildPreviewLocationUpdateEvent(netConnection, location).call();

        if(event.isCanceled()) {
            return;
        }

        // Send to the player if they can build.
        netConnection.sendCanBuildHere(event.isCanBuild());
    }
}
