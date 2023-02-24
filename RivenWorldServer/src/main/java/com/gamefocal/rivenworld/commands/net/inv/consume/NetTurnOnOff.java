package com.gamefocal.rivenworld.commands.net.inv.consume;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.InventoryService;

import java.util.UUID;

@Command(name = "noof", sources = "tcp")
public class NetTurnOnOff extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        Inventory inventory = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(message.args[0]));

        if (inventory != null) {

            UUID attachedEntity = inventory.getAttachedEntity();

            System.out.println("ATTACHED: " + attachedEntity);

            if (attachedEntity != null) {
                // Has a attached entity

                GameEntityModel m = DedicatedServer.instance.getWorld().getEntityFromId(attachedEntity);

                if (m != null) {

                    GameEntity e = m.entityData;

                    if (InteractableEntity.class.isAssignableFrom(e.getClass())) {

                        System.out.println("TOGGLE");

                        // is a Interactable Entity
                        ((InteractableEntity) e).onInteract(netConnection, InteractAction.TOGGLE_ON_OFF, netConnection.getPlayer().equipmentSlots.getWeapon());
                    }

                }

            }

        }

    }
}
