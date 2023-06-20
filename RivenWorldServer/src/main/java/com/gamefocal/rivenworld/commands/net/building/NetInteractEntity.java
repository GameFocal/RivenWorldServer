package com.gamefocal.rivenworld.commands.net.building;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.interact.PlayerInteractEvent;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.models.GameEntityModel;

import java.util.UUID;

@Command(name = "ninte", sources = "tcp")
public class NetInteractEntity extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        UUID entityUUID = UUID.fromString(message.args[0]);

//        System.out.println(entityUUID);

        GameEntityModel model = DedicatedServer.instance.getWorld().getEntityFromId(entityUUID);

        if (model != null) {

            // Check location bounds
            if (model.location.dist(netConnection.getPlayer().location) <= 300) {
                // Within range for this to happen.

                if (InteractableEntity.class.isAssignableFrom(model.entityData.getClass())) {

                    PlayerInteractEvent event = new PlayerInteractEvent(netConnection,InteractAction.USE,model.location,model.entityData).call();
                    if(event.isCanceled()) {
                        return;
                    }

                    InteractableEntity i = (InteractableEntity) model.entityData;
                    i.onInteract(netConnection, InteractAction.USE, netConnection.getPlayer().equipmentSlots.getWeapon());
                }
            }
        }
    }
}
