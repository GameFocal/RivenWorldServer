package com.gamefocal.rivenworld.models;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.data.DataSource;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.placable.decoration.BedPlaceable;
import com.gamefocal.rivenworld.game.enviroment.player.PlayerStats;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.game.inventory.equipment.EquipmentSlots;
import com.gamefocal.rivenworld.game.inventory.hotbar.PlayerHotbar;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.serializer.JsonDataType;
import com.gamefocal.rivenworld.serializer.LocationDataType;
import com.gamefocal.rivenworld.service.PlayerService;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
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

    @DatabaseField(persisterClass = LocationDataType.class,canBeNull = true)
    public Location location = new Location(0, 0, 0);

    @DatabaseField(persisterClass = JsonDataType.class)
    public Inventory inventory = new Inventory(InventoryType.PLAYER, "Player Inventory", "self", 27, 6);

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

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = true, foreignColumnName = "id")
    public GameGuildModel invitedToJoinGuild = null;

    @ForeignCollectionField
    public Collection<GameEntityModel> owned;

    @ForeignCollectionField
    public Collection<GamePlayerSkillsModel> skills;

    public PlayerModel() {
        this.inventory.setHasEquipment(true);
//        this.inventory.getCraftingQueue().addAllowedRecipes(
//                new WoodenClubRecipe(),
//                new StoneHatchetRecipe(),
//                new LandClaimPlaceableRecipe(),
//                new WorkBenchPlaceableRecipe()
//        );
    }

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
        // Check Inventory
        for (InventoryStack s : this.inventory.getItems()) {
            if (s != null && s.getItem().getItemUUID() == itemUUID) {
                return s;
            }
        }

        return null;
    }

    public boolean isOnline() {
        return DedicatedServer.get(PlayerService.class).players.containsKey(UUID.fromString(this.uuid));
    }

    public HiveNetConnection getActiveConnection() {
        if (this.isOnline()) {
            return DedicatedServer.get(PlayerService.class).players.get(UUID.fromString(this.uuid));
        }
        return null;
    }

}
