package com.gamefocal.island.commands.net.building;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.building.BlockPlaceEvent;
import com.gamefocal.island.game.entites.blocks.TestBlock;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.island.game.util.Location;

@Command(name = "blockp", sources = "tcp")
public class NetPlaceBlock extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // TODO: Do checks here.

        System.out.println(message.toString());

        if (message.args.length == 2) {
            // Ex: blockp|{locstr}|{name}

            BlockPlaceEvent placeEvent = new BlockPlaceEvent(Location.fromString(message.args[0]), message.args[1]);

            if (placeEvent.isCanceled()) {
                return;
            }

            // TODO: Find what item is in the hand, and spawn that item here.

            // TODO: Add ability to trigger the interact call on the placable item in hand. Then spawn the item from the getEntity call on the item.

            // Check to see if item in hand
            InventoryStack stack = netConnection.getPlayer().equipmentSlots.getWeapon();
            if (stack != null) {
                // Has a item in hand.

                InventoryItem item = stack.getItem();
                if (PlaceableInventoryItem.class.isAssignableFrom(item.getClass())) {
                    // Is a placable item

                    // TODO: Add event

                    // TODO: Remove from Inventory and update

                    // TODO: If stack empty, unequip item.

                    DedicatedServer.instance.getWorld().spawn(((PlaceableInventoryItem<?>) item).spawnItem(), Location.fromString(message.args[0]));
                }

            }

//            DedicatedServer.instance.getWorld().spawn(new TestBlock(), Location.fromString(message.args[0]));
        }
    }
}
