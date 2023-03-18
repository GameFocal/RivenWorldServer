package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.entites.special.KingWarChest;
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

    public static final Location warChestLocation = Location.fromString("70379.266,111036.15,24494.129,0.0,0.0,80.0");
    public static final Location throneLocation = Location.fromString("68385.69,111753.164,24708.893,0.0,0.0,-143.69724");
    public static final Location managmentTable = Location.fromString("70727.12,111609.65,24589.777,0.0,0.0,-62.15799");
    public static PlayerModel isTheKing;
    public static KingWarChest warChest;

    public static float taxPer30Mins = 5;

    public static String kingdomName = "Iron Kingdom";

    public static PlayerModel claiming = null;

    public static Long beganClaimAt = 0L;

    public static LinkedList<Location> castleChunks = new LinkedList<>();

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

        sendKingdomMessage("The King has ruled to change the kingdom name to " + kingdomName + " and a new tax rate of " + taxPer30Mins);
    }

    public static void startClaim(HiveNetConnection connection) {
        claiming = connection.getPlayer();
        beganClaimAt = System.currentTimeMillis();

//        KingService.playKingAnnouncement();
        KingService.sendKingdomMessage(connection.getPlayer().displayName + " is claiming the throne");

        connection.sendChatMessage(ChatColor.GREEN + "You've began to claim the throne, you must stay near the throne and alive for the next 30 seconds.");

        TaskService.scheduledDelayTask(() -> {
            if (KingService.claiming != null && connection.getPlayer().uuid.equalsIgnoreCase(KingService.claiming.uuid)) {
                KingService.finishClaim();
            }
        }, TickUtil.SECONDS(30), false);
    }

    public static void releaseClaim() {
        if (isTheKing != null) {
            KingService.sendKingdomMessage("The king has abdicated the throne, this land is without a ruler. All taxes will remain until a new king rises.");
            isTheKing = null;
            releaseCastleChunks();
        }
    }

    public static void releaseCastleChunks() {
        for (Location l : castleChunks) {
            WorldChunk c = DedicatedServer.instance.getWorld().getChunk(l.getX(), l.getY());
            DedicatedServer.get(ClaimService.class).releaseChunkFromClaim(c.getModel());
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
        taxPer30Mins = Float.parseFloat(GameMetaModel.getMetaValue("tax-rate", "5"));

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

        Location a = Location.fromString("36.0,64.0,0.0,0.0,0.0,0.0");
        Location b = Location.fromString("40.0,55.0,0.0,0.0,0.0,0.0");
//
        ArrayList<Location> locations = LocationUtil.get2DLocationsBetween(a, b);
        castleChunks.addAll(locations);
    }

}
