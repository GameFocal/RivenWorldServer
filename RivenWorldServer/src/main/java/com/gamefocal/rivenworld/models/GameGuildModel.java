package com.gamefocal.rivenworld.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable(tableName = "guild")
public class GameGuildModel {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String name;

    @DatabaseField
    public String color;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = true, foreignColumnName = "id")
    public PlayerModel owner;

    @ForeignCollectionField
    public Collection<PlayerModel> members;

}
