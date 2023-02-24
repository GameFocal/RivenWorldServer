package com.gamefocal.rivenworld.serializer;

import com.gamefocal.rivenworld.DedicatedServer;
import com.google.gson.JsonElement;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

public class JsonDataType extends StringType {

    private static final JsonDataType INSTANCE = new JsonDataType();

    protected JsonDataType() {
        super(SqlType.STRING, new Class[]{JsonElement.class});
    }

    public static JsonDataType getSingleton() {
        return INSTANCE;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        return DedicatedServer.gson.toJson(javaObject, fieldType.getType());
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return DedicatedServer.gson.fromJson((String) sqlArg, fieldType.getType());
    }
}
