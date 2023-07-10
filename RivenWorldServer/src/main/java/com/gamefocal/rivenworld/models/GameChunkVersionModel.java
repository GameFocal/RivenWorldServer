package com.gamefocal.rivenworld.models;

import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.serializer.LocationDataType;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "chunk_version")
public class GameChunkVersionModel {

    @DatabaseField(id = true, persisterClass = LocationDataType.class)
    public Location id;

    @DatabaseField(dataType = DataType.STRING)
    public String versionHash;

}
