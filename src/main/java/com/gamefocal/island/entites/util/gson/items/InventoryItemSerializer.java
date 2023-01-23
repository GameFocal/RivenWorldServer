package com.gamefocal.island.entites.util.gson.items;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.util.Location;
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
