package com.gamefocal.rivenworld.commands.net.building;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.building.BlockDestroyEvent;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.models.GameEntityModel;

import java.util.UUID;

@Command(name = "blockd", sources = "tcp")
public class NetPlaceDestroy extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {


        UUID entityUUID = UUID.fromString(message.args[0]);

        if (DedicatedServer.instance.getWorld().hasEntityOfUUID(entityUUID)) {
            GameEntityModel m = DedicatedServer.instance.getWorld().getEntityFromId(entityUUID);
            if (m.owner.uuid.equalsIgnoreCase(netConnection.getPlayer().uuid)) {

                if (new BlockDestroyEvent(netConnection, m.location, m.entityData).call().isCanceled()) {
                    return;
                }

                DedicatedServer.instance.getWorld().despawn(entityUUID);

                if (m.entityData.getRelatedItem() != null) {
                    InventoryItem i = m.entityData.getRelatedItem();
                    netConnection.getPlayer().inventory.add(i);
                    netConnection.displayItemAdded(new InventoryStack(i));
                }

            } else {

                /*
                 * Guild member check
                 * */
                WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(m.entityData.location);

                if (DedicatedServer.instance.getWorld().getChunk(m.entityData.location) != null) {
                    if (chunk.canBuildInChunk(netConnection, true)) {
                        if (new BlockDestroyEvent(netConnection, m.location, m.entityData).call().isCanceled()) {
                            return;
                        }

                        DedicatedServer.instance.getWorld().despawn(entityUUID);

                        if (m.entityData.getRelatedItem() != null) {
                            InventoryItem i = m.entityData.getRelatedItem();
                            netConnection.getPlayer().inventory.add(i);
                            netConnection.displayItemAdded(new InventoryStack(i));
                        }
                    }
                }

                // Can not despawn
                netConnection.sendChatMessage("" + ChatColor.RED + "You can not delete this item");
            }
        }
    }
}
