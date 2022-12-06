package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.inv.InventoryCloseEvent;
import com.gamefocal.island.events.inv.InventoryOpenEvent;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.storage.StorageEntity;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.items.StoneHatchet;
import com.gamefocal.island.game.items.TestCube;
import com.gamefocal.island.game.items.ammo.WoodenArrow;
import com.gamefocal.island.game.items.weapons.Bow;
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

            System.out.println("[INV]: SELF, OPEN");

            // DEBUG
            if (!netConnection.getPlayer().inventory.hasOfType(StoneHatchet.class)) {
                netConnection.getPlayer().inventory.add(new StoneHatchet());
                netConnection.getPlayer().inventory.add(new TestCube(), 32);
                netConnection.getPlayer().inventory.add(new Bow(), 1);
                netConnection.getPlayer().inventory.add(new WoodenArrow(), 64);
            }

            InventoryOpenEvent event = new InventoryOpenEvent(netConnection.getPlayer().inventory, netConnection).call();

            if (event.isCanceled()) {
                return;
            }

            netConnection.openInventory(netConnection.getPlayer().inventory, true);
        } else {
            for (UUID m : DedicatedServer.instance.getWorld().entites.keySet()) {
                System.out.println(m);
            }

            if (DedicatedServer.instance.getWorld().entites.containsKey(UUID.fromString(inv))) {

                // Is a entity
                GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(UUID.fromString(inv));
                if (StorageEntity.class.isAssignableFrom(e.entityData.getClass())) {

                    StorageEntity se = (StorageEntity) e.entityData;

                    Inventory playerInv = netConnection.getPlayer().inventory;

//                    if (playerInv != null) {
//                        System.out.println("Updating Player Inv.");
//                        netConnection.updateInventory(playerInv);
//                    }

//                    Thread.sleep(1);

//                    netConnection.openInventory(netConnection.getPlayer().inventory,true);
//                    Thread.sleep(1);
//                    netConnection.closeInventory(netConnection.getPlayer().inventory);

                    System.out.println("Opening the Inv.");
//                    netConnection.openInventory(playerInv, true);
                    netConnection.openInventory(se.getInventory(), true);
//
//                    Thread.sleep(1);
//
//                    netConnection.updateInventory(netConnection.getPlayer().inventory);
                }
            }
        }

    }
}
