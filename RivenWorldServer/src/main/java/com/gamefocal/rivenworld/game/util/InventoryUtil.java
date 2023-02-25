package com.gamefocal.rivenworld.game.util;

import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingJob;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

public class InventoryUtil {

    public static JsonObject inventoryToJson(Inventory inventory) {
        JsonObject inv = new JsonObject();
        inv.addProperty("slots", inventory.getStorageSpace());
        inv.addProperty("stack", inventory.getMaxStack());
        inv.addProperty("uuid", inventory.getUuid().toString());
        inv.addProperty("name", inventory.getName());
        inv.addProperty("locked", inventory.isLocked());
        inv.addProperty("type", inventory.getType().index());
        inv.addProperty("hasEquipment", inventory.isHasEquipment());
        inv.addProperty("hasOnOff", inventory.isHasOnOff());
        inv.addProperty("vendor", inventory.isVendor());
        inv.addProperty("loot", inventory.isLootChest());

//        if (inventory.getType() == InventoryType.PLAYER) {
//            inv.addProperty("type", "p");
//        } else if (inventory.getType() == InventoryType.TRANSFER) {
//            inv.addProperty("type", "t");
//        } else if (inventory.getType() == InventoryType.CRAFTING) {
//            inv.addProperty("type", "c");
//        } else {
//            inv.addProperty("type", "-");
//        }

        JsonArray items = new JsonArray();

        int slotIndex = 0;
        for (InventoryStack stack : inventory.getItems()) {
            if (stack != null) {
                JsonObject i = itemToJson(stack, slotIndex);

                items.add(i);
            }
            slotIndex++;
        }

        inv.add("i", items);

        if (inventory.canCraft()) {
            inv.add("crafting", craftingQueueToJson(inventory.getCraftingQueue()));
        }

        return inv;
    }

    public static JsonObject craftingQueueToJson(CraftingQueue queue) {
        JsonArray o = new JsonArray();
        for (CraftingJob job : queue.getJobArray()) {
            if (job != null) {
                JsonObject j = new JsonObject();
                j.addProperty("uuid", job.getUuid().toString());
                j.addProperty("percent", job.percentComplete());
                j.add("item", itemToJson(job.previewStack(), 0));
                o.add(j);
            } else {
                JsonObject j = new JsonObject();
                j.addProperty("uuid", "empty");
                j.addProperty("percent", 0.00);
                j.add("uuid", new JsonObject());
                o.add(j);
            }
        }

        JsonObject q = new JsonObject();
        q.add("jobs", o);

        return q;
    }

    public static JsonObject itemToJson(InventoryStack stack, int index) {
        JsonObject i = new JsonObject();
        i.addProperty("slug", stack.getItem().slug());
        i.addProperty("name", stack.getItem().getClass().getSimpleName());
        i.addProperty("hash", stack.getHash());
        i.addProperty("amt", stack.getAmount());
        i.addProperty("weight", stack.getItem().getWeight());
        i.addProperty("index", index);

        JsonObject m = new JsonObject();
        for (Map.Entry<String, String> m1 : stack.getItem().getMeta().entrySet()) {
            m.addProperty(m1.getKey(), m1.getValue());
        }

        i.add("meta", m);

        return i;
    }

}
