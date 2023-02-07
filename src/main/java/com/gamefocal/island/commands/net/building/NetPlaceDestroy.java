package com.gamefocal.island.commands.net.building;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.building.BlockDestroyEvent;
import com.gamefocal.island.events.building.BlockPlaceEvent;
import com.gamefocal.island.game.entites.blocks.TestBlock;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.DataService;

import java.util.List;

@Command(name = "blockd", sources = "tcp")
public class NetPlaceDestroy extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
//        System.out.println(message.toString());

        Location destroyLoc = Location.fromString(message.args[0]);

        List<GameEntityModel> model = DataService.gameEntities.queryForEq("location", destroyLoc);

        if (model.size() > 0) {

            for (GameEntityModel m : model) {

                BlockDestroyEvent event = new BlockDestroyEvent(netConnection, destroyLoc, m.entityData).call();
                if(event.isCanceled()) {
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

                    DedicatedServer.instance.getWorld().despawn(m.uuid);

                } else {
                    netConnection.sendChatMessage(ChatColor.RED + "You are not owner of this block.");
                }
            }

        } else {
            System.out.println("Not found.");
        }

    }
}
