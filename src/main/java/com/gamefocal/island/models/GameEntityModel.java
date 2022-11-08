package com.gamefocal.island.models;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.service.NetworkService;
import com.google.gson.Gson;
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
        HiveNetMessage msg = new HiveNetMessage();
        msg.cmd = "od";
        msg.args = new String[]{this.uuid.toString()};

        DedicatedServer.get(NetworkService.class).broadcastUdp(msg, null);
    }

    public void sync() {
        Gson gson = new Gson();
        String json = gson.toJson(this);

        HiveNetMessage msg = new HiveNetMessage();
        msg.cmd = "ou";
        msg.args = new String[]{this.uuid.toString(), this.location.toString(), this.entityData.unrealClass, json};

        DedicatedServer.get(NetworkService.class).broadcastUdp(msg, null);
    }

}
