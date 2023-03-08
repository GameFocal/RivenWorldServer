package com.gamefocal.rivenworld.commands.net.building;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.building.PropAttemptPlaceEvent;
import com.gamefocal.rivenworld.events.building.PropPlaceEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameEntityModel;

import java.util.UUID;

@Command(name = "propp", sources = "tcp")
public class NetPlaceProp extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {


        if (message.args.length >= 2) {
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
                    spawnItem.uuid = UUID.randomUUID();

                    Location spawnLocation = Location.fromString(message.args[0]);

                    PropAttemptPlaceEvent placeEvent = new PropAttemptPlaceEvent(spawnLocation, netConnection, spawnItem).call();

                    if (placeEvent.isCanceled()) {
                        return;
                    }

                    stack.remove(1);
                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PLACE_ITEM, spawnLocation, 5, 1, 1);
                    netConnection.updatePlayerInventory();
                    netConnection.syncEquipmentSlots();
                    if (stack.getAmount() < 0) {
                        // Remove if it is below 0
                        netConnection.getPlayer().equipmentSlots.setWeapon(null);
                    } else {

                        // Add meta data if there
                        if (message.args.length >= 3) {
                            // You have meta :)
                            for (int i = 2; i < message.args.length; i++) {
                                String[] p = message.args[i].split(",");
                                placeEvent.getProp().setMeta(p[0], p[1]);
                            }
                        }

                        GameEntityModel model = DedicatedServer.instance.getWorld().spawn(placeEvent.getProp(), placeEvent.getLocation(), netConnection);

                        // Send spawn command
                        if (model != null) {
//                            model.syncState(netConnection);
                            new PropPlaceEvent(model.location, netConnection, model.entityData).call();
                        }

                        netConnection.getPlayer().equipmentSlots.setWeapon(stack);
                    }
                }
            }
        }
    }
}
