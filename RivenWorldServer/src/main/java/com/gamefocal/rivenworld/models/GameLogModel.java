package com.gamefocal.rivenworld.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.DateTime;

@DatabaseTable
public class GameLogModel {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = true, foreignColumnName = "id")
    public GameChunkModel chunkModel;

    @DatabaseField
    public String type;

    @DatabaseField
    public String data;

    @DatabaseField
    public DateTime time;

}
