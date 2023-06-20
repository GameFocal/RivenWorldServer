package com.gamefocal.rivenworld.commands.net.building;

import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.building.BuildPreviewLocationUpdateEvent;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.util.Location;

@Command(name = "nprev", sources = "udp")
public class NetPreviewLocation extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.getInHand() != null && PlaceableInventoryItem.class.isAssignableFrom(netConnection.getInHand().getItem().getClass())) {

            Location location = Location.fromString(message.args[0]);
            netConnection.setBuildPreviewLocation(location);

            BuildPreviewLocationUpdateEvent event = new BuildPreviewLocationUpdateEvent(netConnection, location).call();

            if (event.isCanceled()) {
                return;
            }

            // Send to the player if they can build.
            netConnection.sendCanBuildHere(event.isCanBuild());
        }
    }
}
