package com.gamefocal.island.models;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.serializer.JsonDataType;
import com.gamefocal.island.serializer.LocationDataType;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.DateTime;

import java.util.UUID;

@DatabaseTable(tableName = "game_resource_node")
public class GameResourceNode {

    @DatabaseField(id = true)
    public String uuid;

    @DatabaseField(persisterClass = LocationDataType.class)
    public Location location;

    @DatabaseField(persisterClass = LocationDataType.class,canBeNull = true)
    public Location realLocation = null;

    @DatabaseField(persisterClass = JsonDataType.class)
    public GameEntity spawnEntity;

    @DatabaseField
    public boolean spawned = false;

    @DatabaseField
    public float spawnDelay = 18000;

    @DatabaseField
    public Long nextSpawn = 0L;

    @DatabaseField(canBeNull = true)
    public UUID attachedEntity = null;

}
