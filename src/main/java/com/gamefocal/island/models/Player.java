package com.gamefocal.island.models;

import com.gamefocal.island.entites.data.DataSource;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.DateTime;

@DataSource(idType = String.class)
@DatabaseTable(tableName = "player")
public class Player {

    @DatabaseField(id = true)
    public String id;

    @DatabaseField(unique = true, canBeNull = false)
    public String uuid;

    @DatabaseField()
    public DateTime lastSeenAt;

    @DatabaseField()
    public DateTime firstSeenAt;

    @DatabaseField()
    public String location;

}
