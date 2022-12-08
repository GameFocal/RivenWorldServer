package com.gamefocal.island.commands.net.building;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.building.BlockPlaceEvent;
import com.gamefocal.island.game.entites.TestBlock;
import com.gamefocal.island.game.util.Location;

@Command(name = "blockd", sources = "tcp")
public class NetPlaceDestroy extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // TODO: Do checks here.

        // Ex: blockp|{locstr}|{name}

        BlockPlaceEvent placeEvent = new BlockPlaceEvent(Location.fromString(message.args[0]), message.args[1]);

        if (placeEvent.isCanceled()) {
            return;
        }

        // TODO: Find what item is in the hand, and spawn that item here.

        // TODO: Add ability to trigger the interact call on the placable item in hand. Then spawn the item from the getEntity call on the item.

        DedicatedServer.instance.getWorld().spawn(new TestBlock(), Location.fromString(message.args[0]));
    }
}
