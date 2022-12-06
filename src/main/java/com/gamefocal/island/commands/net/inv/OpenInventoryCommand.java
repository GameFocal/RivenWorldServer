package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.inv.InventoryCloseEvent;
import com.gamefocal.island.events.inv.InventoryOpenEvent;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.storage.StorageEntity;
import com.gamefocal.island.game.items.StoneHatchet;
import com.gamefocal.island.game.items.TestCube;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.InventoryService;

import java.util.UUID;

@Command(name = "invopen", sources = "tcp")
public class OpenInventoryCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // Open the inventory.
        String inv = message.args[0];

        if (inv.equalsIgnoreCase("self")) {
            // The player inv

            // DEBUG
            if (!netConnection.getPlayer().inventory.hasOfType(StoneHatchet.class)) {
                netConnection.getPlayer().inventory.add(new StoneHatchet());
                netConnection.getPlayer().inventory.add(new TestCube(), 2);
            }

            InventoryOpenEvent event = new InventoryOpenEvent(netConnection.getPlayer().inventory, netConnection).call();

            if (event.isCanceled()) {
                return;
            }

            netConnection.openInventory(netConnection.getPlayer().inventory, true);
        } else {
            System.out.println("Open Other: " + inv);

            System.out.println(DedicatedServer.instance.getWorld().entites.size());

            for (UUID m : DedicatedServer.instance.getWorld().entites.keySet()) {
                System.out.println(m);
            }

            if (DedicatedServer.instance.getWorld().entites.containsKey(UUID.fromString(inv))) {
                System.out.println("not null");

                // Is a entity
                GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(UUID.fromString(inv));
                if (StorageEntity.class.isAssignableFrom(e.entityData.getClass())) {

                    System.out.println("Is Storage.");

                    StorageEntity se = (StorageEntity) e.entityData;

                    netConnection.openInventory(se.getInventory(), true);
                }
            } else {
                System.out.println("Can not find netid for entity...");
            }
        }

    }
}
