package com.gamefocal.rivenworld.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "guild_members")
public class GameGuildMemberModel {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = true, foreignColumnName = "id")
    public GameGuildModel guildModel;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = true, foreignColumnName = "id")
    public PlayerModel playerModel;

    @DatabaseField
    public long createdAt;

}
