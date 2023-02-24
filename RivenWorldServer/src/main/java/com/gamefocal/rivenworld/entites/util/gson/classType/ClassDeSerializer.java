package com.gamefocal.rivenworld.entites.util.gson.classType;

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
