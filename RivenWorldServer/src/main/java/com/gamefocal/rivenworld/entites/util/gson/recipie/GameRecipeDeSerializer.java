package com.gamefocal.rivenworld.entites.util.gson.recipie;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GameRecipeDeSerializer implements JsonDeserializer<CraftingRecipe> {
    @Override
    public CraftingRecipe deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        try {
            JsonObject o = jsonElement.getAsJsonObject();
            Type c = Class.forName(o.get("class").getAsString());
            return DedicatedServer.gson.fromJson(o, c);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
