package com.gamefocal.rivenworld.entites.util.gson.entity;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.GameEntity;
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
