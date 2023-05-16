package com.gamefocal.rivenworld.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable(tableName = "land_claims")
public class GameLandClaimModel {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public float fuel = 0;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false, foreignColumnName = "uuid")
    public PlayerModel owner;

    @DatabaseField
    public Long createdAt;

    @DatabaseField
    public boolean guildCanBuild = true;

    @DatabaseField
    public boolean guildCanInteract = true;

    @ForeignCollectionField
    public Collection<GameChunkModel> chunks;

    public float maxFuel() {
        return (150 * this.chunks.size());
    }
}
