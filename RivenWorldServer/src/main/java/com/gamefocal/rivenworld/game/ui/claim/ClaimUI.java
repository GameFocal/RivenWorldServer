package com.gamefocal.rivenworld.game.ui.claim;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.entites.placable.LandClaimEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.InventoryUtil;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.models.GameLandClaimModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.InventoryService;
import com.gamefocal.rivenworld.service.KingService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.joda.time.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClaimUI extends GameUI<LandClaimEntity> {
    @Override
    public String name() {
        return "claim";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, LandClaimEntity obj) {
        connection.updatePlayerInventory();

        try {
            DataService.landClaims.refresh(obj.getLandClaim());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JsonObject claimData = new JsonObject();

        claimData.addProperty("fuel", obj.getLandClaim().fuel);
        claimData.addProperty("totalFuel", (864 * obj.getLandClaim().chunks.size()));

//        DateTime now = DateTime.now();
//        DateTime out = now.plusMinutes((int) ((obj.getLandClaim().fuel / KingService.taxPer30Mins) * 30));
//
//        float d = Days.daysBetween(now, out).getDays();
//        float h = Hours.hoursBetween(now, out).getHours();
//        float m = Minutes.minutesBetween(now, out).getMinutes();
//        float s = Seconds.secondsBetween(now, out).getSeconds();
//
//        String timeLeft = "~";
//
//        if (d > 1) {
//            timeLeft = d + " Day(s)";
//        } else if (h > 1) {
//            timeLeft = d + " Hour(s)";
//        } else if (m > 1) {
//            timeLeft = m + " Min(s)";
//        } else if (s > 1) {
//            timeLeft = s + " Sec(s)";
//        }

        String timeLeft = "";

        claimData.addProperty("timeLeft", timeLeft);
        claimData.addProperty("percent", obj.getLandClaim().fuel / (864 * obj.getLandClaim().chunks.size()));
        claimData.addProperty("build", "Only Me");
        claimData.addProperty("interact", "Only Me");
        claimData.addProperty("tax", KingService.taxPer30Mins);
        claimData.addProperty("name", (obj.getLandClaim().owner.guild != null) ? obj.getLandClaim().owner.guild.name : obj.getLandClaim().owner.displayName + "'s Claim");
        claimData.addProperty("size", obj.getLandClaim().chunks.size());

        JsonObject main = new JsonObject();
        main.add("fuel", obj.fuelInventory.toJson());
        main.add("claim", claimData);

        return main;
    }

    @Override
    public void onOpen(HiveNetConnection connection, LandClaimEntity object) {
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
        DedicatedServer.get(InventoryService.class).trackInventory(object.fuelInventory);

        connection.getPlayer().inventory.attachToUI(this);
        object.fuelInventory.attachToUI(this);
    }

    @Override
    public void onClose(HiveNetConnection connection, LandClaimEntity object) {
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
                DataService.landClaims.refresh(this.getAttached().getLandClaim());
                this.getAttached().getLandClaim().guildCanBuild = guildCanBuild;
                this.getAttached().getLandClaim().guildCanInteract = guildCanInteract;
                DataService.landClaims.update(this.getAttached().getLandClaim());

                this.update(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else if(tag.equalsIgnoreCase("toggleBorders")) {
            System.out.println("recv toggle border command");

            LandClaimEntity landClaimEntity = this.getAttached();
            Collection<GameChunkModel> chunks = landClaimEntity.getLandClaim().chunks;

            ArrayList<WorldChunk> worldChunks = new ArrayList<>();
            for (GameChunkModel chunkModel : chunks) {
                worldChunks.add(DedicatedServer.instance.getWorld().getChunk(chunkModel.id.getX(),chunkModel.id.getY()));
            }

            // TODO: Loop through the worldChunks and get data :)
            // chunks are 2400 24x24 blocks
            if (!connection.displayborder) {
                JsonObject o = new JsonObject();
                JsonArray borderpoints = new JsonArray();
                for (WorldChunk chunk : worldChunks) {
                    Location centerLoc = chunk.getCenter();
                    borderpoints.add(new Location(centerLoc.getX()+1200, centerLoc.getY()+1200, 0).toString());
                    borderpoints.add(new Location(centerLoc.getX()-1200, centerLoc.getY()+1200, 0).toString());
                    borderpoints.add(new Location(centerLoc.getX()-1200, centerLoc.getY()-1200, 0).toString());
                    borderpoints.add(new Location(centerLoc.getX()+1200, centerLoc.getY()-1200, 0).toString());
                }
                o.add("points", borderpoints);
                connection.showClaimBorder(o, Color.BLUE);
            } else {
                System.out.println("disable");
                connection.hideClaimBorder();
            }

        }
    }
}
