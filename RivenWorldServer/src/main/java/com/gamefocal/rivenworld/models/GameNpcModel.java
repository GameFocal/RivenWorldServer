package com.gamefocal.rivenworld.models;

import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.serializer.JsonDataType;
import com.gamefocal.rivenworld.serializer.LocationDataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "game_npc")
public class GameNpcModel {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String npcType;

    @DatabaseField(persisterClass = LocationDataType.class)
    public Location location;

    @DatabaseField(canBeNull = true)
    public UUID spawnedId;

}
