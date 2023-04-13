package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.resources.Stump;
import com.gamefocal.rivenworld.game.foliage.FoliageState;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.hit.FoliageHitResult;
import com.gamefocal.rivenworld.game.skills.skillTypes.ForagingSkill;
import com.gamefocal.rivenworld.game.skills.skillTypes.WoodcuttingSkill;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.tasks.HiveTaskSequence;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.google.auto.service.AutoService;
import com.google.gson.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

import javax.inject.Singleton;
import java.sql.SQLException;

@Singleton
@AutoService(HiveService.class)
public class FoliageService implements HiveService<FoliageService> {

    private JsonArray foliageCache = new JsonArray();

    public static String getHash(String name, String locStr) {
        return DigestUtils.md5Hex(name + ":" + locStr);
    }

    @Override
    public void init() {
    }

    public float getStartingHealth(String name) {
        if (name.contains("Medium")) {
            // Media Tree
            return 100;
        } else if (name.contains("Large")) {
            // Large Tree
            return 250;
        } else if (name.contains("Saplings")) {
            // Small Tree
            return 15;
        }

        return 25;
    }

    public void regrowTreeFromStump(GameEntity entity, boolean force) {
        if (Stump.class.isAssignableFrom(entity.getClass())) {

            Stump stump = (Stump) entity;

            try {
                GameFoliageModel foliageModel = DataService.gameFoliage.queryBuilder().where().eq("attachedEntity", entity).queryForFirst();

                if (foliageModel != null) {
                    foliageModel.foliageState = FoliageState.GROWN;
                    foliageModel.health = this.getStartingHealth(foliageModel.modelName);
                    foliageModel.attachedEntity = null;
                    DataService.gameFoliage.update(foliageModel);

                    // Despawn stump
                    DedicatedServer.instance.getWorld().despawn(stump.uuid);
                } else {
                    System.out.println("Unable to find model...");
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }

    public void harvest(FoliageHitResult hitResult, HiveNetConnection connection) {
        String hash = FoliageService.getHash(hitResult.getName(), hitResult.getFoliageLocation().toString());
        try {
            GameFoliageModel f = DataService.gameFoliage.queryForId(hash);

            if (f == null) {
                f = new GameFoliageModel();
                f.uuid = hash;
                f.modelName = hitResult.getName();
                f.foliageIndex = hitResult.getIndex();
                f.foliageState = FoliageState.GROWN;
                f.health = DedicatedServer.get(FoliageService.class).getStartingHealth(hitResult.getName());
                f.growth = 100;
                f.location = hitResult.getFoliageLocation();

                DataService.gameFoliage.createOrUpdate(f);
            }

            float hitValue = 1;

            InventoryStack inHand = connection.getPlayer().equipmentSlots.inHand;
            if (inHand != null) {
                // Has something in their hand
                if (Hatchet.class.isAssignableFrom(inHand.getItem().getClass())) {
                    Hatchet hatchet = (Hatchet) inHand.getItem();
                    hitValue = hatchet.hit();
                } else if (Pickaxe.class.isAssignableFrom(inHand.getItem().getClass())) {
                    hitValue = 0;
                }
            }

            if (hitValue < 0) {
                f.syncToPlayer(connection, true);
                return;
            }

            if (hitValue > 0) {

                final float hv = hitValue;

                float oldHealth = f.health;

                if (!connection.inHandDurability(2)) {
                    return;
                }

                f.health -= hitValue;

                InventoryStack give = new InventoryStack(new WoodLog(), (int) (hitValue / 5) * 2);

                final InventoryStack giveF = give;
                final GameFoliageModel ff = f;

                SkillService.addExp(connection, WoodcuttingSkill.class, 2);

                connection.playAnimation(Animation.SWING_AXE);
                HiveTaskSequence hiveTaskSequence = new HiveTaskSequence(false);
                hiveTaskSequence.await(20L);
                hiveTaskSequence.exec(() -> {
                    connection.showFloatingTxt("-" + hv, hitResult.getHitLocation());
                }).exec((() -> {
                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.TREE_HIT, hitResult.getHitLocation(), 5, 1f, 1f);
                })).exec(() -> {
                    if (giveF != null) {
                        connection.getPlayer().inventory.add(giveF);
                        connection.displayItemAdded(giveF);
                    }
                }).exec(() -> {
//                                foliageModel.syncToPlayer(connection, true);
                }).exec(() -> {
                    if (ff.health <= 0) {
                        Stump stump = new Stump();
                        DedicatedServer.instance.getWorld().spawn(stump, ff.location);

                        ff.foliageState = FoliageState.CUT;
                        ff.growth = 0.00f;
                        ff.attachedEntity = stump;

                        ff.syncToPlayer(connection, true);
                    }

                    try {
                        DataService.gameFoliage.update(ff);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });

                TaskService.scheduleTaskSequence(hiveTaskSequence);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public JsonArray getFoliageCache() {
        return foliageCache;
    }
}
