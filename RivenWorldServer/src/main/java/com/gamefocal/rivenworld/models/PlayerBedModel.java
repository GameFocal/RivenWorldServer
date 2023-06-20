package com.gamefocal.rivenworld.models;

import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.serializer.JsonDataType;
import com.gamefocal.rivenworld.serializer.LocationDataType;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "player_beds")
public class PlayerBedModel {

    @DatabaseField(id = true, dataType = DataType.UUID)
    public UUID id;

    @DatabaseField(persisterClass = LocationDataType.class)
    public Location bedLocation;

    @DatabaseField(dataType = DataType.UUID)
    public UUID bedEntityUUID;

}
