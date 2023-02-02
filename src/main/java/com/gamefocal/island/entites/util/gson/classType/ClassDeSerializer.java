package com.gamefocal.island.entites.util.gson.classType;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ClassDeSerializer implements JsonDeserializer<Class> {
    @Override
    public Class deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return Class.forName(jsonElement.getAsString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
