package com.gamefocal.island.models;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.util.Location;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "game_entity")
public class GameEntityModel {

    public boolean isDirty = false;

    @DatabaseField(generatedId = true, dataType = DataType.UUID)
    public UUID uuid;

    @DatabaseField
    public String entityType;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public GameEntity<?> entityData;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public Location location;

    public <T> T getEntity(Class<T> type) {
        return (T) this.entityData;
    }

    public void despawn() {
//        this.entityData.
        System.out.println("TODO: Add this");
        // TODO: Add despawn call for the entity
    }

    public void sync() {
        this.entityData.syncAll();
    }

}
