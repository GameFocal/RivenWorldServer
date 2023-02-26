package com.gamefocal.rivenworld.models;

import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.serializer.InventoryDataType;
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

    @DatabaseField(persisterClass = InventoryDataType.class)
    public Inventory runeStorage = new Inventory(3);

    @ForeignCollectionField
    public Collection<GameChunkModel> chunks;

}
