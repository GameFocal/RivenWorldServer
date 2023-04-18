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
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.hit.FoliageHitResult;
import com.gamefocal.rivenworld.game.skills.skillTypes.WoodcuttingSkill;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.tasks.HiveTaskSequence;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.google.auto.service.AutoService;
import com.google.gson.JsonArray;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Singleton
@AutoService(HiveService.class)
public class FoliageService implements HiveService<FoliageService> {

    public static Long lastTreeGrowth = 0L;

    private JsonArray foliageCache = new JsonArray();

    private ConcurrentHashMap<String, GameFoliageModel> foliage = new ConcurrentHashMap<>();

    public static String getHash(String name, String locStr) {
        return DigestUtils.md5Hex(name + ":" + locStr);
    }

    @Override
    public void init() {
        System.out.println("Loading Foliage...");
        try {
            for (GameFoliageModel foliageModel : DataService.gameFoliage.queryForAll()) {
                DataService.exec(() -> {
//                    System.out.println("Loading Foliage #" + foliageModel.uuid);
                    foliage.put(foliageModel.uuid, foliageModel);
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<String, GameFoliageModel> getFoliage() {
        return foliage;
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

    public GameFoliageModel getFoliage(FoliageHitResult hitResult) {
        return this.getFoliage(getHash(hitResult.getName(), hitResult.getFoliageLocation().toString()));
    }

    public GameFoliageModel getFoliage(String name, Location location) {
        return this.getFoliage(getHash(name, location.toString()));
    }

    public GameFoliageModel getFoliage(String hash) {
        if (this.foliage.containsKey(hash)) {
            return this.foliage.get(hash);
        }

        return null;
    }

    public void growTree(FoliageHitResult hitResult) {
        try {
            GameFoliageModel f = this.getFoliage(hitResult);

            if (f != null) {

                f.growth += 25;
                f.health += 25;
                if (f.growth > 100) {
                    f.growth = 100;
                }

//                DataService.gameFoliage.update(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void regrowTreeFromStump(GameEntity entity, boolean force) {
        if (Stump.class.isAssignableFrom(entity.getClass())) {
            Stump stump = (Stump) entity;

            if (stump.getAttachedFoliageId() != null) {
                GameFoliageModel foliageModel = this.getFoliage(stump.getAttachedFoliageId());
                foliageModel.foliageState = FoliageState.GROWN;
                foliageModel.health = this.getStartingHealth(foliageModel.modelName);
                foliageModel.attachedEntity = null;
                foliageModel.growth = 25;
                foliageModel.lastGrowthTick = DateTime.now();
//                DataService.gameFoliage.update(foliageModel);


                DedicatedServer.instance.getWorld().despawn(stump.uuid);
            }

//                GameFoliageModel foliageModel = DataService.gameFoliage.queryBuilder().where().eq("attachedEntity", entity).queryForFirst();


//                if (foliageModel != null) {
//                    foliageModel.foliageState = FoliageState.GROWN;
//                    foliageModel.health = this.getStartingHealth(foliageModel.modelName);
//                    foliageModel.attachedEntity = null;
//                    foliageModel.growth = 25;
//                    foliageModel.lastGrowthTick = DateTime.now();
//                    DataService.gameFoliage.update(foliageModel);
//
//                    // Despawn stump
//                    DedicatedServer.instance.getWorld().despawn(stump.uuid);
//                }

        } else {
//            System.out.println("No Stump Found (In Regrow)");
        }
    }

    public void spawnTreeAt(String typeName, Location location) {
        String hash = FoliageService.getHash(typeName, location.toString());
        try {
//            GameFoliageModel f = DataService.gameFoliage.queryForId(hash);
            // TODO: Spawn a tree on demand here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void growTick() {
        try {
            for (GameFoliageModel foliageModel : foliage.values()) {

                /*
                * DataService.gameFoliage
                    .queryBuilder().where().isNotNull("attachedEntity").or().gt("growth", 0).and().lt("growth", 100).query()
                * */

                if (foliageModel.attachedEntity != null || (foliageModel.growth > 0 && foliageModel.growth < 100)) {

                    // Get the spawn time for the entity
                    GameEntity entity = foliageModel.attachedEntity;
                    if (entity != null) {

                        if (DedicatedServer.instance.getWorld().getEntityFromId(entity.uuid) == null) {
                            foliageModel.attachedEntity = null;
//                        DataService.gameFoliage.update(foliageModel);
                            continue;
                        }

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
                        if (TimeUnit.MILLISECONDS.toHours(diffInMillis) >= 4) {
                            foliageModel.growth += 25;
                            foliageModel.health += 25;
                            if (foliageModel.growth > 100) {
                                foliageModel.growth = 100;
                            }
                            foliageModel.lastGrowthTick = DateTime.now();
//                            DataService.gameFoliage.update(foliageModel);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(FoliageHitResult hitResult) {
//        String hash = FoliageService.getHash(hitResult.getName(), hitResult.getFoliageLocation().toString());
        try {
//            GameFoliageModel f = DataService.gameFoliage.queryForId(hash);

            GameFoliageModel f = this.getFoliage(hitResult);

            registerNewFoliage(hitResult);

//            if (f == null) {
//                f = new GameFoliageModel();
//                f.uuid = getHash(hitResult.getName(), hitResult.getFoliageLocation().toString());
//                f.modelName = hitResult.getName();
//                f.foliageIndex = hitResult.getIndex();
//                f.foliageState = FoliageState.NEW;
//                f.health = DedicatedServer.get(FoliageService.class).getStartingHealth(hitResult.getName());
//                f.growth = 100;
//                f.location = hitResult.getFoliageLocation();
//
//                DataService.gameFoliage.createOrUpdate(f);
//            }

            Stump stump = new Stump(f.uuid);
            DedicatedServer.instance.getWorld().spawn(stump, f.location);

            f.foliageState = FoliageState.CUT;
            f.growth = 0.00f;
            f.attachedEntity = stump;

//            DataService.gameFoliage.createOrUpdate(f);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float maxHealth(GameFoliageModel foliageModel) {
        float h = getStartingHealth(foliageModel.modelName);
        if (foliageModel.foliageState != FoliageState.NEW) {
            h += (25 * (foliageModel.growth / 25));
        }
        return h;
    }

    public void registerNewFoliage(FoliageHitResult hitResult) {
        GameFoliageModel f = this.getFoliage(hitResult);

        if (f == null) {
            f = new GameFoliageModel();
            f.uuid = getHash(hitResult.getName(), hitResult.getFoliageLocation().toString());
            f.modelName = hitResult.getName();
            f.foliageIndex = hitResult.getIndex();
            f.foliageState = FoliageState.NEW;
            f.health = DedicatedServer.get(FoliageService.class).getStartingHealth(hitResult.getName());
            f.growth = 100;
            f.location = hitResult.getFoliageLocation();

            foliage.put(f.uuid, f);

//            DataService.gameFoliage.createOrUpdate(f);
        }
    }

    public void harvest(FoliageHitResult hitResult, HiveNetConnection connection) {
        String hash = FoliageService.getHash(hitResult.getName(), hitResult.getFoliageLocation().toString());
//            GameFoliageModel f = DataService.gameFoliage.queryForId(hash);

        registerNewFoliage(hitResult);

        GameFoliageModel f = this.getFoliage(hitResult);

//            if (f == null) {
//                f = new GameFoliageModel();
//                f.uuid = hash;
//                f.modelName = hitResult.getName();
//                f.foliageIndex = hitResult.getIndex();
//                f.foliageState = FoliageState.NEW;
//                f.health = DedicatedServer.get(FoliageService.class).getStartingHealth(hitResult.getName());
//                f.growth = 100;
//                f.location = hitResult.getFoliageLocation();
//
//                DataService.gameFoliage.createOrUpdate(f);
//            }

        float hitValue = 1;
        float produces = 0;

        InventoryStack inHand = connection.getPlayer().equipmentSlots.inHand;

        if (inHand == null) {
//            f.health -= 1;
            hitValue = 1;
            produces = RandomUtil.getRandomChance(.25f) ? 1 : 0;
        }

        if (inHand != null) {
            // Has something in their hand
            if (Hatchet.class.isAssignableFrom(inHand.getItem().getClass())) {
                Hatchet hatchet = (Hatchet) inHand.getItem();
                hitValue = hatchet.hit();
                produces = Math.max(1, hatchet.hit() / 4);
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

            f.health -= 5;

            connection.flashProgressBar("Tree", f.health / this.maxHealth(f), Color.RED, 5);

            InventoryStack give = new InventoryStack(new WoodLog(), (int) produces);

            final InventoryStack giveF = give;
            final GameFoliageModel ff = f;

            if (produces > 0) {
                SkillService.addExp(connection, WoodcuttingSkill.class, 2);
            }

            if (inHand == null) {
                connection.playAnimation(Animation.PUNCH);
            } else {
                connection.playAnimation(Animation.SWING_AXE);
            }

            HiveTaskSequence hiveTaskSequence = new HiveTaskSequence(false);
            hiveTaskSequence.await(20L);
            hiveTaskSequence.exec(() -> {
                connection.showFloatingTxt("-" + hv, hitResult.getHitLocation());
            }).exec((() -> {
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.TREE_HIT, hitResult.getHitLocation(), 5, 1f, 1f);
            })).exec(() -> {
                if (giveF != null && giveF.getAmount() > 0) {
                    connection.getPlayer().inventory.add(giveF);
                    connection.displayItemAdded(giveF);
                }
            }).exec(() -> {
//                                foliageModel.syncToPlayer(connection, true);
            }).exec(() -> {
                if (ff.health <= 0) {
                    Stump stump = new Stump(f.uuid);
                    DedicatedServer.instance.getWorld().spawn(stump, ff.location);

                    ff.foliageState = FoliageState.CUT;
                    ff.growth = 0.00f;
                    ff.attachedEntity = stump;

                    ff.syncToPlayer(connection, true);
                }

//                try {
//                    DataService.gameFoliage.update(ff);
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
            });

            TaskService.scheduleTaskSequence(hiveTaskSequence);
        }
    }

    public void save() {
        for (GameFoliageModel foliageModel : this.foliage.values()) {
            DataService.exec(() -> {
                try {
                    DataService.gameFoliage.createOrUpdate(foliageModel);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public JsonArray getFoliageCache() {
        return foliageCache;
    }
}
