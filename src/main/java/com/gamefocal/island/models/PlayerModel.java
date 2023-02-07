package com.gamefocal.island.models;

import com.gamefocal.island.entites.data.DataSource;
import com.gamefocal.island.game.enviroment.player.PlayerStats;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.inventory.crafting.CraftingQueue;
import com.gamefocal.island.game.inventory.hotbar.PlayerHotbar;
import com.gamefocal.island.game.inventory.equipment.EquipmentSlot;
import com.gamefocal.island.game.inventory.equipment.EquipmentSlots;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryType;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.serializer.JsonDataType;
import com.gamefocal.island.serializer.LocationDataType;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Hashtable;
import java.util.UUID;

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

    @DatabaseField(persisterClass = LocationDataType.class)
    public Location location = new Location(0, 0, 0);

    @DatabaseField(persisterClass = JsonDataType.class)
    public Inventory inventory = new Inventory(InventoryType.PLAYER, "Player Inventory", "self", 27,6);

    @DatabaseField(persisterClass = JsonDataType.class)
    public EquipmentSlots equipmentSlots = new EquipmentSlots();

    @DatabaseField(persisterClass = JsonDataType.class)
    public PlayerHotbar hotbar = new PlayerHotbar();

    @DatabaseField(persisterClass = JsonDataType.class)
    public Hashtable<String, String> meta = new Hashtable<>();

    @DatabaseField(persisterClass = JsonDataType.class)
    public PlayerStats playerStats = new PlayerStats();

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = true, foreignColumnName = "id")
    public GameGuildModel guild = null;

    @ForeignCollectionField
    public Collection<GameEntityModel> owned;

    public boolean isFishing() {
        if (this.meta.containsKey("fishing")) {
            return this.meta.get("fishing").equalsIgnoreCase("1");
        }

        return false;
    }

    public void setIsFishing(boolean is) {
        this.meta.put("fishing", (is) ? "1" : "0");
    }

    public InventoryStack findStackFromUUID(UUID itemUUID) {
        // Check equipment
        for (EquipmentSlot s : EquipmentSlot.values()) {
            InventoryStack stack = this.equipmentSlots.getItemBySlot(s);
            if (stack != null && stack.getItem().getItemUUID() == itemUUID) {
                return stack;
            }
        }

        // Check Inventory
        for (InventoryStack s : this.inventory.getItems()) {
            if (s != null && s.getItem().getItemUUID() == itemUUID) {
                return s;
            }
        }

        return null;
    }

}
