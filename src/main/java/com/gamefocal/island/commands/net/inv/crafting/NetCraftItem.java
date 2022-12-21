package com.gamefocal.island.commands.net.inv.crafting;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.*;
import com.gamefocal.island.game.inventory.crafting.CraftingJob;
import com.gamefocal.island.game.tasks.seqence.ExecSequenceAction;
import com.gamefocal.island.game.util.InventoryUtil;
import com.gamefocal.island.service.InventoryService;
import com.gamefocal.island.service.TaskService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Command(name = "invcs")
public class NetCraftItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        String invId = message.args[0];
        String itemSlug = message.args[1];
        int customAmt = Integer.parseInt(message.args[2]);
        String action = message.args[3];
        String destInventoryId = message.args[4];

        Inventory sourceInventory = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(invId));
        Inventory destInventory = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(destInventoryId));

        if (sourceInventory == null || destInventory == null) {
            return;
        }

        if (sourceInventory != null) {
            int amtToCraft = 0;

            if (action.equalsIgnoreCase("Craft")) {
                amtToCraft = 1;
            } else if (action.equalsIgnoreCase("Craft 5")) {
                amtToCraft = 5;
            } else if (action.equalsIgnoreCase("Craft 10")) {
                amtToCraft = 10;
            } else if (action.equalsIgnoreCase("Craft Custom Amount")) {
                amtToCraft = customAmt;
            }

            Class makeType = DedicatedServer.get(InventoryService.class).getItemClassFromSlug(itemSlug);
            if (makeType != null) {

                // Found the type from Slug

                InventoryItem item = (InventoryItem) makeType.newInstance();

                if (InventoryCraftingInterface.class.isAssignableFrom(item.getClass())) {
                    // has a crafting interface

                    InventoryCraftingInterface craftingInterface = (InventoryCraftingInterface) item;

                    CraftingRecipe recipe = craftingInterface.canCraft(netConnection);
                    if (recipe != null) {

                        // See how much they can make
                        int canMake = sourceInventory.canCraftAmt(recipe);

                        System.out.println("CAN MAKE: " + canMake);

                        if (amtToCraft > canMake) {
                            amtToCraft = canMake;
                        }

                        // See if they selected Craft All, if so craft all.
                        if (action.equalsIgnoreCase("Craft All")) {
                            amtToCraft = canMake;
                        }

                        System.out.println("AMT TO CRAFT: " + amtToCraft);

                        // Make Job
                        CraftingJob job = new CraftingJob(sourceInventory, recipe, amtToCraft);
                        sourceInventory.getCraftingQueue().queueJob(job);

                        // Remove Resource from Inventory.
                        for (int i = 0; i < amtToCraft; i++) {
                            for (Map.Entry<Class<? extends InventoryItem>, Integer> e : recipe.getRequires().entrySet()) {
                                sourceInventory.removeOfType(e.getKey(), e.getValue());
                            }
                        }

                        // Send Updates
                        TaskService.scheduleTaskSequence(false, new ExecSequenceAction() {
                            @Override
                            public void run() {
                                netConnection.updateInventory(sourceInventory);
                            }
                        });
                    }
                }

            }
        }

    }
}
