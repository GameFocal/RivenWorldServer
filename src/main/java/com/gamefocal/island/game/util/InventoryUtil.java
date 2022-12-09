package com.gamefocal.island.game.util;

import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.inventory.InventoryType;
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

        if (inventory.getType() == InventoryType.PLAYER) {
            inv.addProperty("type", "p");
        } else if (inventory.getType() == InventoryType.TRANSFER) {
            inv.addProperty("type", "t");
        } else if (inventory.getType() == InventoryType.CRAFTING) {
            inv.addProperty("type", "c");
        } else {
            inv.addProperty("type", "-");
        }

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

        return inv;
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
