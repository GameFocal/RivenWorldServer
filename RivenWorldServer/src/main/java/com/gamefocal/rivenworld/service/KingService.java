package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.game.entites.special.KingWarChest;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.gamefocal.rivenworld.models.GameMetaModel;
import com.gamefocal.rivenworld.models.PlayerModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

@Singleton
@AutoService(HiveService.class)
public class KingService implements HiveService<KingService> {

    public static final Location warChestLocation = Location.fromString("71137.66,114966.086,25177.432,0.0,0.0,40.0");
    public static final Location throneLocation = Location.fromString("72452.89,115241.28,25355.506,0.0,0.0,-17.23352");
    public static final Location managmentTable = Location.fromString("71296.67,114775.04,25287.26,0.0,0.0,-122.75374");
    public static PlayerModel isTheKing;
    public static KingWarChest warChest;

    public static float taxPer30Mins = 1f;

    public static String kingdomName = "Iron Kingdom";

    public static PlayerModel claiming = null;

    public static Long beganClaimAt = 0L;

    public static LinkedList<Location> castleChunks = new LinkedList<>();

    public static Inventory kingInventory = null;

    public static BoundingBox throneBound() {
        return ShapeUtil.makeBoundBox(throneLocation.toVector(), 200, 500);
    }

    public static BoundingBox managmentBound() {
        return ShapeUtil.makeBoundBox(managmentTable.toVector(), 200, 500);
    }

    public static void updateKingdom(String name, float tax) {
        kingdomName = name;
        taxPer30Mins = tax;

        GameMetaModel.setMetaValue("kingdom-name", kingdomName);
        GameMetaModel.setMetaValue("tax-rate", String.valueOf(tax));
        playKingAnnouncement();

        sendKingdomMessage("The King has ruled to change the kingdom name to " + kingdomName + " and a new tax rate of " + Math.min(100, Math.round(taxPer30Mins * 100)) + "% for a upkeep cost of " + DedicatedServer.get(ClaimService.class).upkeepCost() + " per chunk per 24 hours.");
    }

    public static void startClaim(HiveNetConnection connection) {
        claiming = connection.getPlayer();
        beganClaimAt = System.currentTimeMillis();

//        KingService.playKingAnnouncement();
        KingService.sendKingdomMessage(connection.getPlayer().displayName + " is claiming the throne");

        TaskService.schedulePlayerInterruptTask(KingService::finishClaim, 30L, "Claiming the Throne", Color.GOLD, connection, () -> {
            KingService.claiming = null;
            KingService.beganClaimAt = 0L;
            connection.sendChatMessage("You got to far away from the throne so your claim attempt failed");
        });

//        connection.sendChatMessage(ChatColor.GREEN + "You've began to claim the throne, you must stay near the throne and alive for the next 30 seconds.");
//
//        TaskService.scheduledDelayTask(() -> {
//            if (KingService.claiming != null && connection.getPlayer().uuid.equalsIgnoreCase(KingService.claiming.uuid)) {
//                KingService.finishClaim();
//            }
//        }, TickUtil.SECONDS(30), false);
    }

    public static void releaseClaim() {
        if (isTheKing != null) {
            KingService.sendKingdomMessage("The king has abdicated the throne, this land is without a ruler. All taxes will remain until a new king rises.");
            isTheKing = null;
            releaseCastleChunks();
        }
    }

    public static void releaseCastleChunks() {
        if (DedicatedServer.settings.lockKingCastleChunks) {
            for (Location l : castleChunks) {
                WorldChunk c = DedicatedServer.instance.getWorld().getChunk(l.getX(), l.getY());
                DedicatedServer.get(ClaimService.class).releaseChunkFromClaim(c.getModel(), true);
            }
        }
    }

    public static void finishClaim() {
        if (claiming != null) {
            KingService.playKingAnnouncement();
            KingService.sendKingdomMessage("The throne has been taken by " + claiming.displayName);

            isTheKing = claiming;
            claiming = null;
            beganClaimAt = 0L;

            releaseCastleChunks();

            GameMetaModel.setMetaValue("king", isTheKing.id);
        }
    }

    public static void sendKingdomMessage(String msg) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            connection.sendChatMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "From the Throne: " + ChatColor.RESET + msg);
        }
    }

    public static void playKingAnnouncement() {
        DedicatedServer.instance.getWorld().playSoundToAllPlayers(GameSounds.KING_TRUMPET, .75f, 1f);
    }

    @Override
    public void init() {
        kingdomName = GameMetaModel.getMetaValue("kingdom-name", "Iron Kingdom");
        taxPer30Mins = Float.parseFloat(GameMetaModel.getMetaValue("tax-rate", "1"));

        if (taxPer30Mins > 1) {
            taxPer30Mins = 1;
        }

        if (GameMetaModel.hasMeta("king")) {
            try {
                PlayerModel playerModel = DataService.players.queryForId(GameMetaModel.getMetaValue("king", null));
                if (playerModel != null) {
                    isTheKing = playerModel;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /*
         * Sync the castle chunks
         * */
        // 36.0,64.0,0.0,0.0,0.0,0.0
        // 40.0,55.0,0.0,0.0,0.0,0.0

        if (DedicatedServer.settings.lockKingCastleChunks) {
            Location a = Location.fromString("41.0,64.0,0.0,0.0,0.0,0.0");
            Location b = Location.fromString("35.0,56.0,0.0,0.0,0.0,0.0");

            ArrayList<Location> locations = LocationUtil.get2DLocationsBetween(a, b);
            castleChunks.addAll(locations);
//            ClaimService.lockChunksBetween(a, b);
        }
    }

}
