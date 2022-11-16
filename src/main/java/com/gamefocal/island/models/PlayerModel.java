package com.gamefocal.island.models;

import com.gamefocal.island.entites.data.DataSource;
import com.gamefocal.island.game.util.Location;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.DateTime;

@DataSource(idType = String.class)
@DatabaseTable(tableName = "player")
public class PlayerModel {

    @DatabaseField(id = true)
    public String id;

    @DatabaseField(unique = true, canBeNull = false)
    public String uuid;

    @DatabaseField()
    public DateTime lastSeenAt;

    @DatabaseField()
    public DateTime firstSeenAt;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public Location location = new Location(0, 0, 0);

}