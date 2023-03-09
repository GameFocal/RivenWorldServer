package com.gamefocal.rivenworld.entites.util.gson.recipie;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class GameRecipeSerializer implements JsonSerializer<CraftingRecipe> {
    @Override
    public JsonElement serialize(CraftingRecipe entity, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject o = DedicatedServer.gson.toJsonTree(entity, entity.getClass()).getAsJsonObject();
        o.addProperty("class", entity.getClass().getName());

        return o;
    }
}
