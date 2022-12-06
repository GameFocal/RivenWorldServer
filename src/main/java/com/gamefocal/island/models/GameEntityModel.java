package com.gamefocal.island.models;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.util.Location;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.UUID;

@DatabaseTable(tableName = "game_entity")
public class GameEntityModel {

    public boolean isDirty = false;

    @DatabaseField(id = true, dataType = DataType.UUID)
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

    public void sync(ArrayList<HiveNetConnection> connections) {
        this.entityData.syncToClients(connections);
    }

    public void sync(HiveNetConnection connection) {
        this.entityData.syncToPlayer(connection);
    }

    public void sync() {
        this.entityData.syncAll();
    }

}
