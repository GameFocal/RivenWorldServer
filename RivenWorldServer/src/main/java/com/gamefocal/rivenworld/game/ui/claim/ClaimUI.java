package com.gamefocal.rivenworld.game.ui.claim;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.InventoryUtil;
import com.gamefocal.rivenworld.models.GameLandClaimModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.InventoryService;
import com.gamefocal.rivenworld.service.KingService;
import com.google.gson.JsonObject;
import org.joda.time.*;

import java.sql.SQLException;

public class ClaimUI extends GameUI<GameLandClaimModel> {
    private Inventory fuel = new Inventory(1);

    @Override
    public String name() {
        return "claim";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, GameLandClaimModel obj) {

        try {
            DataService.landClaims.refresh(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JsonObject plInv = connection.getPlayer().inventory.toJson();
        JsonObject claimData = new JsonObject();

        System.out.println(obj.fuel);

        claimData.addProperty("fuel", obj.fuel);
        claimData.addProperty("totalFuel", (864 * obj.chunks.size()));

        DateTime now = DateTime.now();
        DateTime out = now.plusMinutes((int) ((obj.fuel / KingService.taxPer30Mins) * 30));

        float d = Days.daysBetween(now, out).getDays();
        float h = Hours.hoursBetween(now, out).getHours();
        float m = Minutes.minutesBetween(now, out).getMinutes();
        float s = Seconds.secondsBetween(now, out).getSeconds();

        String timeLeft = "~";

        if (d > 1) {
            timeLeft = d + " Day(s)";
        } else if (h > 1) {
            timeLeft = d + " Hour(s)";
        } else if (m > 1) {
            timeLeft = m + " Min(s)";
        } else if (s > 1) {
            timeLeft = s + " Sec(s)";
        }

        claimData.addProperty("timeLeft", timeLeft);
        claimData.addProperty("percent", obj.fuel / (864 * obj.chunks.size()));
        claimData.addProperty("build", "Only Me");
        claimData.addProperty("interact", "Only Me");
        claimData.addProperty("tax", KingService.taxPer30Mins);

        JsonObject main = new JsonObject();
        main.add("plinv", plInv);
        main.add("objinv", this.fuel.toJson());
        main.add("claim", claimData);
//        main.add("fuel", InventoryUtil.inventoryToJson(obj.runeStorage));
        return main;
    }

    @Override
    public void onOpen(HiveNetConnection connection, GameLandClaimModel object) {

        this.fuel.getTags().put("claim", String.valueOf(object.id));

        this.fuel.setLocked(false);
        this.fuel.setLinkedUI(this);
        this.fuel.setName("Claim Fuel");

//        object.runeStorage.setLinkedUI(this);
        connection.getPlayer().inventory.setLinkedUI(this);
        this.fuel.setLinkedUI(this);

        try {
            this.fuel.takeOwnership(connection, true);
            connection.getPlayer().inventory.takeOwnership(connection, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
//        DedicatedServer.get(InventoryService.class).trackInventory(object.runeStorage);
        DedicatedServer.get(InventoryService.class).trackInventory(this.fuel);
    }

    @Override
    public void onClose(HiveNetConnection connection, GameLandClaimModel object) {

    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("perms")) {

            boolean guildCanBuild = data[0].equalsIgnoreCase("Guild Members");
            boolean guildCanInteract = data[1].equalsIgnoreCase("Guild Members");

            try {
                DataService.landClaims.refresh(this.getAttached());
                this.getAttached().guildCanBuild = guildCanBuild;
                this.getAttached().guildCanInteract = guildCanInteract;
                DataService.landClaims.update(this.getAttached());

                this.update(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
