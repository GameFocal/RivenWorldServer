package com.gamefocal.island.commands.net.combat;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;

import java.util.UUID;

@Command(name = "nhit", sources = "tcp")
public class NetHitEntity extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // Hit a entity
        System.out.println(message.toString());

        UUID entityUUID = UUID.fromString(message.args[0]);
        Location location = Location.fromString(message.args[1]);

        GameEntityModel entity = DedicatedServer.instance.getWorld().getEntityFromId(entityUUID);

        if (entity != null) {

            // Check location bounds
            if (entity.location.dist(location) <= 300) {
                // Within range for this to happen.

                if (InteractableEntity.class.isAssignableFrom(entity.entityData.getClass())) {
                    InteractableEntity i = (InteractableEntity) entity.entityData;
                    i.onInteract(netConnection, InteractAction.HIT.setLocation(location),netConnection.getPlayer().equipmentSlots.getWeapon());
                }
            }

        }

    }
}
