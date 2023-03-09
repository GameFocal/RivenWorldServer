package com.gamefocal.rivenworld.entites.util.gson.classr;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GameClassDeSerializer implements JsonDeserializer<Class> {
    @Override
    public Class deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        if (jsonElement.isJsonPrimitive()) {
            try {
                return Class.forName(jsonElement.getAsString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
