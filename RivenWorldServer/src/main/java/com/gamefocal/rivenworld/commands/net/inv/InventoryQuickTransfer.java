package com.gamefocal.rivenworld.commands.net.inv;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.ui.inventory.RivenStorageUI;
import com.gamefocal.rivenworld.service.InventoryService;

import java.util.UUID;

@Command(name = "invmq", sources = "tcp")
public class InventoryQuickTransfer extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        Inventory playerInventory = netConnection.getPlayer().inventory;

        Inventory inventory = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(message.args[0]));
        int slotNumberFrom = Integer.parseInt(message.args[1]);

        if (inventory != null) {

            InventoryStack fromItem = inventory.get(slotNumberFrom);
            if (fromItem != null) {

                Inventory pair = null;

                GameUI gameUI = netConnection.getOpenUI();
                if (RivenStorageUI.class.isAssignableFrom(gameUI.getClass())) {
                    // Is a storage UI
                    pair = ((RivenStorageUI) gameUI).getAttached();
                } else if (RivenCraftingUI.class.isAssignableFrom(gameUI.getClass())) {
                    // Crafting UI
                    pair = ((RivenCraftingUI) gameUI).getAttached().dest();
                }

                if (pair == null) {
                    return;
                }

                if (playerInventory.getUuid() == inventory.getUuid()) {

                    /*
                     * From the player inventory
                     * */
                    if (pair.canAdd(fromItem)) {
                        pair.add(fromItem);
                        inventory.clear(slotNumberFrom);
                        inventory.update();
                        pair.update();
                    }
                } else {

                    /*
                     * To the player inventory
                     * */
                    if (playerInventory.canAdd(fromItem)) {
                        playerInventory.add(fromItem);
                        inventory.clear(slotNumberFrom);
                        inventory.update();
                        playerInventory.update();
                    }

                }

                inventory.updateUIs(netConnection);
                pair.updateUIs(netConnection);
//                netConnection.syncEquipmentSlots();

            }
        }
    }
}
