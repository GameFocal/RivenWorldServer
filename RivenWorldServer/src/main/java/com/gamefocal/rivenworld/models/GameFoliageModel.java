package com.gamefocal.rivenworld.models;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.foliage.FoliageState;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.serializer.JsonDataType;
import com.gamefocal.rivenworld.serializer.LocationDataType;
import com.google.gson.JsonObject;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@DatabaseTable(tableName = "game_foliage")
public class GameFoliageModel {

    @DatabaseField(id = true)
    public String uuid;

    @DatabaseField
    public String modelName;

    @DatabaseField
    public int foliageIndex;

    @DatabaseField
    public String foliageType;

    @DatabaseField(dataType = DataType.ENUM_TO_STRING)
    public FoliageState foliageState;

    @DatabaseField
    public float health = 100.0f;

    @DatabaseField
    public float growth = 100.00f;

    @DatabaseField(persisterClass = LocationDataType.class)
    public Location location;

    @DatabaseField(persisterClass = JsonDataType.class)
    public GameEntity attachedEntity;

    public void syncToPlayer(HiveNetConnection connection, boolean animate) {
        JsonObject f = new JsonObject();
        f.addProperty("uuid", this.uuid);
        f.addProperty("model", this.modelName);
        f.addProperty("loc", this.location.toString());
        f.addProperty("state", this.foliageState.name());
        f.addProperty("health", this.health);
        f.addProperty("growth", this.growth);
        f.addProperty("i", this.foliageIndex);
        f.addProperty("anim", animate);

        connection.sendTcp("f|" + Base64.getEncoder().encodeToString(f.toString().getBytes(StandardCharsets.UTF_8)));
        connection.getFoliageSync().put(this.uuid,this.stateHash());
    }

    public String stateHash() {
        return DigestUtils.md5Hex(this.uuid + this.modelName + this.foliageState + this.health + this.growth + this.location.toString());
    }

}
