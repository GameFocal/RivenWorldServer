package com.gamefocal.rivenworld.entites.util.gson.items;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
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
