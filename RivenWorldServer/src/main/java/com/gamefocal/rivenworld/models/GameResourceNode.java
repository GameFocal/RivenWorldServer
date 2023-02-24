package com.gamefocal.rivenworld.models;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.serializer.JsonDataType;
import com.gamefocal.rivenworld.serializer.LocationDataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
