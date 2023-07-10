package com.gamefocal.rivenworld.models;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.serializer.JsonDataType;
import com.gamefocal.rivenworld.serializer.LocationDataType;
import com.gamefocal.rivenworld.service.SaveService;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

import java.util.UUID;

@DatabaseTable(tableName = "game_entity")
public class GameEntityModel {

    public transient boolean isDirty = false;

    @DatabaseField(id = true, dataType = DataType.UUID)
    public UUID uuid;

    @DatabaseField
    public String entityType;

    @DatabaseField(persisterClass = JsonDataType.class)
    public GameEntity entityData;

    @DatabaseField(persisterClass = LocationDataType.class)
    public Location location;

    @DatabaseField(persisterClass = LocationDataType.class)
    public Location chunkCords;

    @DatabaseField
    public float health = 100;

    @DatabaseField
    public Long version = 0L;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = true, foreignColumnName = "uuid")
    public PlayerModel owner = null;

    @DatabaseField
    public DateTime createdAt;

    public transient long lastSaveAt = 0L;

    public <T> T getEntity(Class<T> type) {
        return (T) this.entityData;
    }

    public void despawn() {
        this.entityData.despawn();
    }

    public String entityHash() {
        if (this.uuid != null && entityType != null && this.entityData != null) {
            return this.entityData.entityHash();
        }

        return DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis()));
    }

    public void markDirty() {
        this.isDirty = true;
        SaveService.saveQueue.add(this);
    }

    public void clearDirty() {
        this.isDirty = false;
        SaveService.saveQueue.remove(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (GameEntityModel.class.isAssignableFrom(obj.getClass())) {
            return ((GameEntityModel) obj).uuid == this.uuid;
        }
        return false;
    }
}
