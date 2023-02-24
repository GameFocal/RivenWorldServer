package com.gamefocal.rivenworld.entites.util.gson.entity;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.GameEntity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class GameEntitySerializer implements JsonSerializer<GameEntity> {
    @Override
    public JsonElement serialize(GameEntity entity, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject o = DedicatedServer.gson.toJsonTree(entity, entity.getClass()).getAsJsonObject();
        o.addProperty("class", entity.getClass().getName());

        return o;
    }
}
