package com.gamefocal.rivenworld.entites.util.gson;

import com.gamefocal.rivenworld.game.util.Location;
import com.google.gson.*;

import java.lang.reflect.Type;

public class LocationSerializer implements JsonSerializer<Location> {
    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(location.toString());
    }
}
