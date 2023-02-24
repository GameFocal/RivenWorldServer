package com.gamefocal.rivenworld.commands.net.building;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.building.BlockAttemptDestroyEvent;
import com.gamefocal.rivenworld.events.building.BlockDestroyEvent;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.DataService;

import java.util.List;

@Command(name = "blockd", sources = "tcp")
public class NetPlaceDestroy extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        DataService.exec(() -> {

            try {
                Location destroyLoc = Location.fromString(message.args[0]);

                List<GameEntityModel> model = DataService.gameEntities.queryForEq("location", destroyLoc);

                if (model.size() > 0) {

                    for (GameEntityModel m : model) {

                        BlockAttemptDestroyEvent event = new BlockAttemptDestroyEvent(netConnection, destroyLoc, m.entityData).call();
                        if (event.isCanceled()) {
                            continue;
                        }

                        // TODO: Add permission checks in event now.
                        if (m.owner.uuid.equalsIgnoreCase(netConnection.getPlayer().uuid)) {
                            // Is the same player.

//                    m.entityData.
                            if (m.entityData.getRelatedItem() != null) {
                                InventoryItem i = m.entityData.getRelatedItem();
                                netConnection.getPlayer().inventory.add(i);
                                netConnection.displayItemAdded(new InventoryStack(i));
                            }

                            new BlockDestroyEvent(netConnection, m.location, m.entityData).call();

                            DedicatedServer.instance.getWorld().despawn(m.uuid);

                        } else {
                            netConnection.sendChatMessage(ChatColor.RED + "You are not owner of this block.");
                        }
                    }

                } else {
                    System.out.println("Not found.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}
