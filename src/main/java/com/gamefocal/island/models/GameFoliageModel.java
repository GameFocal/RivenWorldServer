package com.gamefocal.island.models;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.foliage.FoliageState;
import com.gamefocal.island.game.util.Location;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "game_foliage")
public class GameFoliageModel {

    @DatabaseField(id = true)
    public String hash;

    @DatabaseField
    public String foliageType;

    @DatabaseField(dataType = DataType.ENUM_TO_STRING)
    public FoliageState foliageState;

    @DatabaseField
    public float health = 100.0f;

    @DatabaseField
    public float growth = 100.00f;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public Location location;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public GameEntity<?> attachedEntity;

}
