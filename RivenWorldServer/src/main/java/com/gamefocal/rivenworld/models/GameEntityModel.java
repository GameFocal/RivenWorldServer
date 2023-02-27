package com.gamefocal.rivenworld.models;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.serializer.JsonDataType;
import com.gamefocal.rivenworld.serializer.LocationDataType;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

import java.util.UUID;

@DatabaseTable(tableName = "game_entity")
public class GameEntityModel {

    public boolean isDirty = false;

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

//    public LinkedList<UUID> playersSubscribed = new LinkedList<>();

    public <T> T getEntity(Class<T> type) {
        return (T) this.entityData;
    }

    public void despawn() {
        this.entityData.despawn();
    }

//    public void sync(ArrayList<HiveNetConnection> connections) {
//        this.entityData.syncToClients(connections);
//    }

//    public void sync(HiveNetConnection connection) {
////        this.entityData.syncToPlayer(connection);
//        this.syncState(connection);
//    }

//    public void markForUpdate() {
//        this.version = System.currentTimeMillis();
//    }

    public String entityHash() {
        if (this.uuid != null && entityType != null && this.entityData != null) {
            return DigestUtils.md5Hex(this.uuid.toString() + "" + entityType + this.entityData.location + version);
        }

        return DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis()));
    }

//    public void syncState(HiveNetConnection connection) {
//        Sphere view = new Sphere(connection.getPlayer().location.toVector(), 20 * 100 * 4); //20 * 100 * 4
//
//        if (view.overlaps(new Sphere(this.location.toVector(), 100))) {
//            if (connection.getLoadedEntites().containsKey(this.uuid)) {
//                // Already is loaded
//
//                String loadedHash = connection.getLoadedEntites().get(this.uuid);
//
////                if(CampFirePlaceable.class.isAssignableFrom(this.entityData.getClass())) {
////                    System.out.println("CH: " + this.entityHash() + ", LH: " + loadedHash);
////                }
//
//                if (!loadedHash.equalsIgnoreCase(this.entityHash())) {
////                    connection.trackEntity(this);
//                    // Has been updated since
////                    this.sync(connection);
//
//                    this.entityData.syncToPlayer(connection);
//                    connection.trackEntity(this);
//                }
//            } else {
//                // Not Tracked
////                this.sync(connection);
//                this.entityData.syncToPlayer(connection);
//                connection.trackEntity(this);
//            }
//        } else {
//            // Trigger a client despawn
//            if (connection.isTrackingEntity(this)) {
//                this.entityData.despawnToPlayer(connection);
////            connection.getLoadedEntites().remove(this);
//                connection.untrackEntity(this);
//            }
//        }
//    }

    @Override
    public boolean equals(Object obj) {
        if (GameEntityModel.class.isAssignableFrom(obj.getClass())) {
            return ((GameEntityModel) obj).uuid == this.uuid;
        }
        return false;
    }
}
