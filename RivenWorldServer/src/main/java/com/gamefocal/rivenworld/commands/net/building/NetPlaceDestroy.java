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
import java.util.UUID;

@Command(name = "blockd", sources = "tcp")
public class NetPlaceDestroy extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {


        UUID entityUUID = UUID.fromString(message.args[0]);

        if (DedicatedServer.instance.getWorld().hasEntityOfUUID(entityUUID)) {
            GameEntityModel m = DedicatedServer.instance.getWorld().getEntityFromId(entityUUID);
            if (m.owner.uuid.equalsIgnoreCase(netConnection.getPlayer().uuid)) {
                DedicatedServer.instance.getWorld().despawn(entityUUID);

                if (m.entityData.getRelatedItem() != null) {
                    InventoryItem i = m.entityData.getRelatedItem();
                    netConnection.getPlayer().inventory.add(i);
                    netConnection.displayItemAdded(new InventoryStack(i));
                }

                new BlockDestroyEvent(netConnection, m.location, m.entityData).call();

            } else {

                // TODO: Add check for guild members with perms for a claim this was built on.


                // Can not despawn
                netConnection.sendChatMessage("" + ChatColor.RED + "You can not delete this item");
            }
        }
    }
}
