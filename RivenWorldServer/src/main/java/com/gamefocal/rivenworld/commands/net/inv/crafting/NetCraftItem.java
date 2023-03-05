package com.gamefocal.rivenworld.commands.net.inv.crafting;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingJob;
import com.gamefocal.rivenworld.game.ui.CraftingUI;
import com.gamefocal.rivenworld.service.InventoryService;

import java.util.Map;

@Command(name = "invcs", sources = "tcp")
public class NetCraftItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println(message.toString());

        String invId = null;
        String itemName = message.args[1];
        float amt = Float.parseFloat(message.args[2]);

        if (netConnection.getOpenUI() != null && CraftingUI.class.isAssignableFrom(netConnection.getOpenUI().getClass())) {

            CraftingUI craftingUI = (CraftingUI) netConnection.getOpenUI();

            // Is within a Crafting UI

            InventoryItem item = DedicatedServer.get(InventoryService.class).getItemClassFromSlug(itemName).newInstance();

            if (InventoryCraftingInterface.class.isAssignableFrom(item.getClass())) {
                // has a crafting interface

                InventoryCraftingInterface craftingInterface = (InventoryCraftingInterface) item;

                CraftingRecipe recipe = craftingInterface.canCraft(netConnection);
                if (recipe != null) {
                    CraftingUI ui = (CraftingUI) netConnection.getOpenUI();

                    int canMake = ui.getSource().canCraftAmt(recipe);
                    if (amt > canMake) {
                        return;
                    }

                    // Make Job
                    CraftingJob job = new CraftingJob(ui.getSource(), ui.getDest(), recipe, (int) amt, craftingUI.getLocation());
                    ui.getDest().getCraftingQueue().queueJob(job);

                    // Remove Resource from Inventory.
                    for (int i = 0; i < amt; i++) {
                        for (Map.Entry<Class<? extends InventoryItem>, Integer> e : recipe.getRequires().entrySet()) {
                            ui.getSource().removeOfType(e.getKey(), e.getValue());
                        }
                    }

                    netConnection.getOpenUI().update(netConnection);
                }
            }
        }
    }
}
