package com.gamefocal.rivenworld.entites.util.gson.items;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.google.gson.*;

import java.lang.reflect.Type;

public class InventoryItemSerializer implements JsonSerializer<InventoryItem> {

    @Override
    public JsonElement serialize(InventoryItem item, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject o = DedicatedServer.gson.toJsonTree(item,item.getClass()).getAsJsonObject();
        o.addProperty("class",item.getClass().getName());

        return o;
    }
}
