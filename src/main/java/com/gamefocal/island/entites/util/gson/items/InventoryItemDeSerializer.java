package com.gamefocal.island.entites.util.gson.items;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.util.Location;
import com.google.gson.*;

import java.lang.reflect.Type;

public class InventoryItemDeSerializer implements JsonDeserializer<InventoryItem> {
    @Override
    public InventoryItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        try {
            JsonObject o = jsonElement.getAsJsonObject();
            Type c = Class.forName(o.get("class").getAsString());
            return DedicatedServer.gson.fromJson(o.toString(),c);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
