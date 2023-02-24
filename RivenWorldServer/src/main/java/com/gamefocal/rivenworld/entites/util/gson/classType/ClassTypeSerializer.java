package com.gamefocal.rivenworld.entites.util.gson.classType;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ClassTypeSerializer implements JsonSerializer<Class> {
    @Override
    public JsonElement serialize(Class aClass, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(aClass.getName());
    }
}
