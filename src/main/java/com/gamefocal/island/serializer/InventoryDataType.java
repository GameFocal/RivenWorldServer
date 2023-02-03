package com.gamefocal.island.serializer;

import com.gamefocal.island.DedicatedServer;
import com.google.gson.JsonElement;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

public class InventoryDataType extends StringType {

    private static final InventoryDataType INSTANCE = new InventoryDataType();

    protected InventoryDataType() {
        super(SqlType.STRING, new Class[]{JsonElement.class});
    }

    public static InventoryDataType getSingleton() {
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