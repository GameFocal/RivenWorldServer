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
    @Override
    public String name() {
        return "claim";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, GameLandClaimModel obj) {

        connection.updatePlayerInventory();

        try {
            DataService.landClaims.refresh(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JsonObject plInv = connection.getPlayer().inventory.toJson();
        JsonObject claimData = new JsonObject();

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
        claimData.addProperty("name", (obj.owner.guild != null) ? obj.owner.guild.name : obj.owner.displayName);
        claimData.addProperty("size", obj.chunks.size());

        JsonObject main = new JsonObject();
        main.add("fuel", obj.fuelInventory.toJson());
        main.add("claim", claimData);
        return main;
    }

    @Override
    public void onOpen(HiveNetConnection connection, GameLandClaimModel object) {
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
        DedicatedServer.get(InventoryService.class).trackInventory(object.fuelInventory);

        connection.getPlayer().inventory.attachToUI(this);
        object.fuelInventory.attachToUI(this);
    }

    @Override
    public void onClose(HiveNetConnection connection, GameLandClaimModel object) {
        connection.getPlayer().inventory.detachFromUI(this);
        object.fuelInventory.detachFromUI(this);
    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("close")) {
            this.close(connection);
        } else if (tag.equalsIgnoreCase("perms")) {

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
