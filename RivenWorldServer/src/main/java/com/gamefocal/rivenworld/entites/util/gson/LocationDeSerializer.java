package com.gamefocal.rivenworld.entites.util.gson;

import com.gamefocal.rivenworld.game.util.Location;
import com.google.gson.*;

import java.lang.reflect.Type;

public class LocationDeSerializer implements JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonPrimitive()) {
            return Location.fromString(jsonElement.getAsString());
        }
        return null;
    }
}
