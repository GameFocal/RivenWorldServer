package com.gamefocal.island.commands.net.building;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.building.BlockAttemptPlaceEvent;
import com.gamefocal.island.events.building.BlockPlaceEvent;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.island.game.sounds.GameSounds;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;

@Command(name = "blockp", sources = "tcp")
public class NetPlaceBlock extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // TODO: Do checks here.

        System.out.println(message.toString());

        if (message.args.length == 2) {
            // Ex: blockp|{locstr}|{name}

            // Check to see if item in hand
            InventoryStack stack = netConnection.getPlayer().equipmentSlots.getWeapon();
            if (stack != null && stack.getAmount() >= 1) {
                // Has a item in hand.

                InventoryItem item = stack.getItem();
                if (PlaceableInventoryItem.class.isAssignableFrom(item.getClass())) {
                    // Is a placable item

                    GameEntity spawnItem = ((PlaceableInventoryItem<?>) item).spawnItem();

                    spawnItem.setRelatedItem(item);

                    Location spawnLocation = Location.fromString(message.args[0]);

                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PLACE_ITEM, spawnLocation, 5, 1, 1);

                    BlockAttemptPlaceEvent placeEvent = new BlockAttemptPlaceEvent(netConnection, spawnLocation, spawnItem).call();

                    if (placeEvent.isCanceled()) {
                        return;
                    }

                    stack.remove(1);

                    if (stack.getAmount() <= 0) {
                        // Remove if it is below 0
                        netConnection.getPlayer().equipmentSlots.setWeapon(null);
                        netConnection.syncEquipmentSlots();
                    } else {
                        GameEntityModel model = DedicatedServer.instance.getWorld().spawn(placeEvent.getBlock(), placeEvent.getLocation(), netConnection);

                        // Send spawn command
                        if (model != null) {
                            model.syncState(netConnection);
                            new BlockPlaceEvent(netConnection, placeEvent.getLocation(), model.entityData).call();
                        }

                        netConnection.getPlayer().equipmentSlots.setWeapon(stack);
                    }
                }
            }
        }
    }
}
