package com.gamefocal.rivenworld.commands.net.fishing;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.service.FishingService;

@Command(name = "fisha", sources = "tcp")
public class NetAttemptCatchFish extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.getPlayer().isFishing()) {
            InventoryItem caught = DedicatedServer.get(FishingService.class).attemptCatchFish(netConnection);
            if (caught != null) {
                // TODO: Spawn the fish in inventory and return to client that they cought something.
                netConnection.sendTcp("fish|true");
            } else {
                // TODO: Send a note that they failed to fish.
                netConnection.sendTcp("fish|false");
            }
        }
    }
}
