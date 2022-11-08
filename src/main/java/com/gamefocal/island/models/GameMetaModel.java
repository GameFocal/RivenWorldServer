package com.gamefocal.island.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class GameMetaModel {

    @DatabaseField(id = true)
    public String name;

    @DatabaseField
    public String value;

}
