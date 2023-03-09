package com.gamefocal.rivenworld.entites.util.gson.classr;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GameClassSerializer implements JsonSerializer<Class> {
    @Override
    public JsonElement serialize(Class src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getName());
    }
}
