package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.game.entites.placable.LandClaimEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Coal;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.GoldIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SilverIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.models.GameLandClaimModel;
import com.gamefocal.rivenworld.models.GameMetaModel;
import com.gamefocal.rivenworld.models.PlayerModel;
import com.google.auto.service.AutoService;
import org.joda.time.DateTime;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
@AutoService(HiveService.class)
public class ClaimService implements HiveService<ClaimService> {

    public static HashMap<Class<? extends InventoryItem>, Float> itemValue = new HashMap<>();

    public static LinkedList<Location> lockedChunks = new LinkedList<>();

    public static void lockChunksBetween(Location a, Location b) {
        ArrayList<Location> locations = LocationUtil.get2DLocationsBetween(a, b);
        lockedChunks.addAll(locations);
    }

    public static void lockChunk(Location a) {
        lockedChunks.add(a);
    }

    public static void lockChunks(Location... locations) {
        lockedChunks.addAll(Arrays.asList(locations));
    }

    @Override
    public void init() {
        itemValue.put(GoldOre.class, 5f);
        itemValue.put(IronOre.class, 5f);
        itemValue.put(GoldIgnot.class, 10f);
        itemValue.put(IronIgnot.class, 10f);
        itemValue.put(SilverIgnot.class, 20f);
        itemValue.put(Coal.class, 1f);
        itemValue.put(WoodLog.class, 2f);
        itemValue.put(WoodStick.class, 1f);
        itemValue.put(Thatch.class, 1f);
        itemValue.put(Fiber.class, 1f);

        TaskService.scheduleRepeatingTask(this::chargeUpkeepCost, TickUtil.MINUTES(60), TickUtil.MINUTES(60), false);
    }

    public void chargeUpkeepCost() {
        long lastUpkeep = 0L;

        if (GameMetaModel.hasMeta("last-upkeep")) {
            // Has a last Upkeep
            lastUpkeep = Long.parseLong(GameMetaModel.getMetaValue("last-upkeep", "0"));
        }

        if (TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - lastUpkeep) >= 24) {

            float upkeep = this.upkeepCost();

            try {
                List<GameLandClaimModel> claims = DataService.landClaims.queryForAll();
                for (GameLandClaimModel landClaimModel : claims) {
                    landClaimModel.fuel -= (upkeep * landClaimModel.chunks.size());

                    if (landClaimModel.fuel < 0) {
                        landClaimModel.fuel = 0;
                    }

                    DataService.landClaims.update(landClaimModel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            GameMetaModel.setMetaValue("last-upkeep", String.valueOf(System.currentTimeMillis()));
        }
    }

    public float upkeepCost() {
        long claimedChunks = 0;
        long totalChunks = 100;
        try {
            claimedChunks = DataService.chunks.queryBuilder().where().isNotNull("claim_id").countOf();
            totalChunks = DataService.chunks.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        float percent = (float) claimedChunks / (float) totalChunks;

        return (float) Math.floor(MathUtils.map(0.0f, 1.0f, 5, 150, percent));
    }

    public boolean canRaidClaim(WorldChunk chunk) {

        if (DedicatedServer.settings.raidMode.equalsIgnoreCase("off")) {
            return false;
        }

        GameChunkModel model = chunk.getModel();

        DateTime now = DateTime.now();

        if (model.claim != null) {
            // Check for online members

            List<PlayerModel> members = new ArrayList<>();
            members.add(model.claim.owner);

            if (!DedicatedServer.settings.raidMode.equalsIgnoreCase("opengame")) {
                if (model.claim.owner.guild != null) {
                    try {
                        members.addAll(DataService.players.queryForEq("guild_id", model.claim.owner.guild.id));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                boolean isOnline = true;

                if (members.size() > 0) {
                    for (PlayerModel m : members) {
                        if (!DedicatedServer.get(PlayerService.class).players.containsKey(UUID.fromString(m.uuid))) {
                            if (m.lastSeenAt.plusSeconds(Math.round(DedicatedServer.settings.raidLogOffCoolDown)).isBefore(now)) {
                                isOnline = false;
                            }
                        }
                    }
                }

                if (!isOnline) {
                    // Check for fuel
                    if (model.claim.fuel > 0) {
                        return false;
                    }
                }

                if (DedicatedServer.settings.raidMode.equalsIgnoreCase("night") && DedicatedServer.get(EnvironmentService.class).isDay) {
                    return false;
                }
            }
        }

        return true;
    }

    public void releaseChunkFromClaim(GameChunkModel chunkModel, boolean despawn) {

        if (chunkModel.claim == null) {
            return;
        }

        boolean transferPrimaryChunk = false;
        // Check if this is a primary chunk if so, transfer it.
        if (chunkModel.isPrimaryChunk) {
            transferPrimaryChunk = true;
            chunkModel.isPrimaryChunk = false;
        }

        try {
            DataService.landClaims.refresh(chunkModel.claim);

            if (chunkModel.claim.chunks.size() == 0) {
                // No more chunks, release claim
                DataService.landClaims.delete(chunkModel.claim);
            } else if (transferPrimaryChunk) {
                for (GameChunkModel c : chunkModel.claim.chunks) {
                    if (!c.isPrimaryChunk) {
                        c.isPrimaryChunk = true;
                        DataService.chunks.update(c);
                        break;
                    }
                }
            }

            if (chunkModel.entityModel != null && despawn) {
                DedicatedServer.instance.getWorld().despawn(chunkModel.entityModel.uuid);
            }

            chunkModel.claim = null;
            chunkModel.entityModel = null;
            DataService.chunks.update(chunkModel);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public GameLandClaimModel claim(HiveNetConnection connection, WorldChunk chunk, LandClaimEntity landClaimEntity) throws SQLException {
        // Claim this chunk for this player.

        GameChunkModel chunkModel = DataService.chunks.queryBuilder().where().eq("id", chunk.getChunkCords()).queryForFirst();
        if (chunkModel != null) {

            // Check to see if this chunk is touching another chunk.
            GameLandClaimModel claimModel = chunk.getRelationClaim(connection);
            if (claimModel == null) {
                // Need to make a new claim.
                claimModel = new GameLandClaimModel();
                claimModel.owner = connection.getPlayer();
                claimModel.fuel = 100;
                claimModel.createdAt = System.currentTimeMillis();

                DataService.landClaims.createOrUpdate(claimModel);
            }

            chunkModel.claim = claimModel;
            chunkModel.entityModel = landClaimEntity.getModel();

            DataService.chunks.createOrUpdate(chunkModel);

            return claimModel;
        }

        return null;
    }

    public boolean canClaim(Location gameLocation, HiveNetConnection by) {
        WorldChunk c = DedicatedServer.instance.getWorld().getChunk(gameLocation);
        if (c != null) {

            if (DedicatedServer.settings.lockKingCastleChunks && KingService.castleChunks.contains(c.getChunkCords()) && (KingService.isTheKing == null || !KingService.isTheKing.uuid.equalsIgnoreCase(by.getPlayer().uuid))) {
                return false;
            }

            if (ClaimService.lockedChunks.contains(c.getChunkCords())) {
                return false;
            }

            try {
                GameChunkModel chunkModel = DataService.chunks.queryBuilder().where().eq("id", c.getChunkCords()).queryForFirst();
                if (chunkModel != null) {
                    return (chunkModel.claim == null);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return false;
    }

}
