package com.gamefocal.island.serializer;

import com.gamefocal.island.game.util.Location;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

public class LocationDataType extends StringType {

    private static final LocationDataType INSTANCE = new LocationDataType();

    protected LocationDataType() {
        super(SqlType.STRING, new Class<?>[]{Location.class});
    }

    public static LocationDataType getSingleton() {
        return INSTANCE;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        return ((Location) javaObject).toString();
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return Location.fromString((String) sqlArg);
    }

}
