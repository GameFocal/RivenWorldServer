package com.gamefocal.island.commands.net.building;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.models.GameEntityModel;

import java.util.UUID;

@Command(name = "ninte", sources = "tcp")
public class NetInteractEntity extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        UUID entityUUID = UUID.fromString(message.args[0]);

        System.out.println(entityUUID);

        GameEntityModel model = DedicatedServer.instance.getWorld().getEntityFromId(entityUUID);

        if (model != null) {

            System.out.println("Found");

            // Check location bounds
            if (model.location.dist(netConnection.getPlayer().location) <= 300) {
                // Within range for this to happen.

                System.out.println("In Range");

                if (InteractableEntity.class.isAssignableFrom(model.entityData.getClass())) {

                    System.out.println("INTER");
                    System.out.println(model.entityData.getClass().getSimpleName());

                    InteractableEntity i = (InteractableEntity) model.entityData;
                    i.onInteract(netConnection, InteractAction.USE, netConnection.getPlayer().equipmentSlots.getWeapon());
                }
            }
        }
    }
}
