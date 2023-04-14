package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.graphics.Color;
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
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.MathUtil;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.google.auto.service.AutoService;
import com.google.gson.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Singleton
@AutoService(HiveService.class)
public class FoliageService implements HiveService<FoliageService> {

    private JsonArray foliageCache = new JsonArray();

    public static String getHash(String name, String locStr) {
        return DigestUtils.md5Hex(name + ":" + locStr);
    }

    @Override
    public void init() {
        TaskService.scheduleRepeatingTask(() -> {
            System.out.println("[TREES]: Growing Trees");
            DedicatedServer.get(FoliageService.class).growTick();
        }, 20L, TickUtil.MINUTES(30), false);
    }

    public float getStartingHealth(String name) {
        if (name.contains("Medium")) {
            // Media Tree
            return 50;
        } else if (name.contains("Large")) {
            // Large Tree
            return 150;
        } else if (name.contains("Saplings")) {
            // Small Tree
            return 15;
        }

        return 25;
    }

    public void growTree(FoliageHitResult hitResult) {
        String hash = FoliageService.getHash(hitResult.getName(), hitResult.getFoliageLocation().toString());
        try {
            GameFoliageModel f = DataService.gameFoliage.queryForId(hash);

            if (f != null) {

                f.growth += 25;
                f.health += 25;
                if (f.growth > 100) {
                    f.growth = 100;
                }

                DataService.gameFoliage.update(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void regrowTreeFromStump(GameEntity entity, boolean force) {
        if (Stump.class.isAssignableFrom(entity.getClass())) {

            Stump stump = (Stump) entity;

            try {
                GameFoliageModel foliageModel = DataService.gameFoliage.queryBuilder().where().eq("attachedEntity", entity).queryForFirst();

                if (foliageModel != null) {

                    System.out.println("Found Foliage...");

                    foliageModel.foliageState = FoliageState.GROWN;
                    foliageModel.health = this.getStartingHealth(foliageModel.modelName);
                    foliageModel.attachedEntity = null;
                    foliageModel.growth = 25;
                    foliageModel.lastGrowthTick = DateTime.now();
                    DataService.gameFoliage.update(foliageModel);

                    // Despawn stump
                    DedicatedServer.instance.getWorld().despawn(stump.uuid);
                } else {
                    System.out.println("Unable to find model...");
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } else {
            System.out.println("No Stump Found (In Regrow)");
        }
    }

    public void spawnTreeAt(String typeName, Location location) {
        String hash = FoliageService.getHash(typeName, location.toString());
        try {
            GameFoliageModel f = DataService.gameFoliage.queryForId(hash);
            // TODO: Spawn a tree on demand here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void growTick() {
        try {
            for (GameFoliageModel foliageModel : DataService.gameFoliage
                    .queryBuilder().where().isNotNull("attachedEntity").or().gt("growth", 0).and().lt("growth", 100).query()) {

                // Get the spawn time for the entity
                GameEntity entity = foliageModel.attachedEntity;
                if (entity != null) {
                    GameEntityModel m = entity.getModel();
                    if (m != null) {
                        long diffInMillis = DateTime.now().getMillis() - m.createdAt.getMillis();
                        if (TimeUnit.MILLISECONDS.toHours(diffInMillis) >= 1) {
                            this.regrowTreeFromStump(entity, true);
                        }
                    }
                } else {
                    // In growth so we need to change it
                    long diffInMillis = DateTime.now().getMillis() - foliageModel.lastGrowthTick.getMillis();
                    if (TimeUnit.MILLISECONDS.toHours(diffInMillis) >= 1) {
                        foliageModel.growth += 25;
                        foliageModel.health += 25;
                        if (foliageModel.growth > 100) {
                            foliageModel.growth = 100;
                        }
                        foliageModel.lastGrowthTick = DateTime.now();
                        DataService.gameFoliage.update(foliageModel);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(FoliageHitResult hitResult) {
        String hash = FoliageService.getHash(hitResult.getName(), hitResult.getFoliageLocation().toString());
        try {
            GameFoliageModel f = DataService.gameFoliage.queryForId(hash);

            if (f == null) {
                f = new GameFoliageModel();
                f.uuid = hash;
                f.modelName = hitResult.getName();
                f.foliageIndex = hitResult.getIndex();
                f.foliageState = FoliageState.NEW;
                f.health = DedicatedServer.get(FoliageService.class).getStartingHealth(hitResult.getName());
                f.growth = 100;
                f.location = hitResult.getFoliageLocation();

                DataService.gameFoliage.createOrUpdate(f);
            }

            Stump stump = new Stump();
            DedicatedServer.instance.getWorld().spawn(stump, f.location);

            f.foliageState = FoliageState.CUT;
            f.growth = 0.00f;
            f.attachedEntity = stump;

            DataService.gameFoliage.createOrUpdate(f);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float maxHealth(GameFoliageModel foliageModel) {
        float h = getStartingHealth(foliageModel.modelName);
        h += (25 * (foliageModel.growth / 25));
        return h;
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
                f.foliageState = FoliageState.NEW;
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

                connection.flashProgressBar("Tree", f.health / this.maxHealth(f), Color.RED, 5);

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
