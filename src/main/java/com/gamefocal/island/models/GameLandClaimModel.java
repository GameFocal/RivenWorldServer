package com.gamefocal.island.models;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.serializer.JsonDataType;
import com.gamefocal.island.serializer.LocationDataType;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@DatabaseTable(tableName = "land_claims")
public class GameLandClaimModel {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public float fuel = 0;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false, foreignColumnName = "uuid")
    public PlayerModel owner;

    @DatabaseField
    public Long createdAt;

    @ForeignCollectionField
    public Collection<GameChunkModel> chunks;

}
