package com.gamefocal.island.commands.net.fishing;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.service.FishingService;

@Command(name = "fisha", sources = "tcp")
public class AttemptCatchFishCommand extends HiveCommand {
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
