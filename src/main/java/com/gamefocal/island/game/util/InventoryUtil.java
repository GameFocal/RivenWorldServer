package com.gamefocal.island.game.util;

import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

public class InventoryUtil {

    public static JsonObject inventoryToJson(Inventory inventory) {
        JsonObject inv = new JsonObject();
        inv.addProperty("slots", inventory.getStorageSpace());
        inv.addProperty("stack", inventory.getMaxStack());


        JsonArray items = new JsonArray();

        for (InventoryStack stack : inventory.getItems()) {
            if (stack != null) {
                JsonObject i = new JsonObject();
                i.addProperty("slug", stack.getItem().slug());
                i.addProperty("name", stack.getItem().getClass().getSimpleName());
                i.addProperty("hash", stack.getHash());
                i.addProperty("amt", stack.getAmount());
                i.addProperty("weight", stack.getItem().getWeight());

                JsonObject m = new JsonObject();
                for (Map.Entry<String, String> m1 : stack.getItem().getMeta().entrySet()) {
                    m.addProperty(m1.getKey(), m1.getValue());
                }

                i.add("meta", m);

                items.add(i);
            }
        }

        inv.add("i", items);

        return inv;
    }

}
