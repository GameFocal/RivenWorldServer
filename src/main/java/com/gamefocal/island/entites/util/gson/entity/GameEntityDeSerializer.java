package com.gamefocal.island.entites.util.gson.entity;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GameEntityDeSerializer implements JsonDeserializer<GameEntity> {
    @Override
    public GameEntity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

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
