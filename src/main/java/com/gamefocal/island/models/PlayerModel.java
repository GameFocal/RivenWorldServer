package com.gamefocal.island.models;

import com.gamefocal.island.entites.data.DataSource;
import com.gamefocal.island.game.inventory.EquipmentSlots;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryType;
import com.gamefocal.island.game.util.Location;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.DateTime;

import java.util.Hashtable;

@DataSource(idType = String.class)
@DatabaseTable(tableName = "player")
public class PlayerModel {

    @DatabaseField(id = true)
    public String id;

    @DatabaseField()
    public String displayName;

    @DatabaseField(unique = true, canBeNull = false)
    public String uuid;

    @DatabaseField()
    public DateTime lastSeenAt;

    @DatabaseField()
    public DateTime firstSeenAt;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public Location location = new Location(0, 0, 0);

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public Inventory inventory = new Inventory(InventoryType.PLAYER, "Player Inventory", "self", 27);

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public EquipmentSlots equipmentSlots = new EquipmentSlots();

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public Hashtable<String, String> meta = new Hashtable<>();

    public boolean isFishing() {
        if (this.meta.containsKey("fishing")) {
            return this.meta.get("fishing").equalsIgnoreCase("1");
        }

        return false;
    }

    public void setIsFishing(boolean is) {
        this.meta.put("fishing", (is) ? "1" : "0");
    }

}
