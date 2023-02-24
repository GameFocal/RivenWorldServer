package com.gamefocal.rivenworld.models;

import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.serializer.LocationDataType;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "chunk")
public class GameChunkModel {

    @DatabaseField(id = true, persisterClass = LocationDataType.class)
    public Location id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = true, foreignColumnName = "id")
    public GameLandClaimModel claim = null;

    @DatabaseField
    public boolean isPrimaryChunk = false;

    @DatabaseField(dataType = DataType.BOOLEAN_INTEGER)
    public boolean inConflict = false;

    @DatabaseField
    public Long conflictStart = 0L;

    @DatabaseField
    public Long conflictTimer = 0L;

    @DatabaseField(foreign = true,foreignAutoRefresh = true,foreignColumnName = "uuid",canBeNull = true)
    public GameEntityModel entityModel;

}
