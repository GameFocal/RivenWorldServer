package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.resources.Stump;
import com.gamefocal.rivenworld.game.foliage.FoliageState;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.IronHatchet;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.SteelHatchet;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.StoneHatchet;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.WoodHatchet;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.player.AnimationCallback;
import com.gamefocal.rivenworld.game.ray.hit.FoliageHitResult;
import com.gamefocal.rivenworld.game.skills.skillTypes.WoodcuttingSkill;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Singleton
@AutoService(HiveService.class)
public class FoliageService implements HiveService<FoliageService> {

    public static Long lastTreeGrowth = 0L;

    public static HashMap<String, Float> defaultHealths = new HashMap<>();
    public static LinkedList<String> noGrowth = new LinkedList<>();

    private JsonArray foliageCache = new JsonArray();

    private ConcurrentHashMap<String, GameFoliageModel> foliage = new ConcurrentHashMap<>();

    public static String getHash(String name, String locStr) {
        return DigestUtils.md5Hex(name + ":" + locStr);
    }

    @Override
    public void init() {
        System.out.println("Loading Foliage...");

        float xsm = 5;
        float sm = 15;
        float md = 50;
        float lg = 150;
        float xlg = 200;

        defaultHealths.put("SM_Tree_Aspen_L_01", md);
        defaultHealths.put("SM_Tree_Aspen_L_02", md);
        defaultHealths.put("SM_Tree_Aspen_M_01", sm);
        defaultHealths.put("SM_Tree_Aspen_S_02", sm);
        defaultHealths.put("SM_Tree_Fir_01", xlg);
        defaultHealths.put("SM_Tree_Fir_02", xlg);
        defaultHealths.put("SM_Tree_Fir_Fallen_03", md);
        defaultHealths.put("SM_Tree_Fir_M_02", lg);
        defaultHealths.put("SM_Tree_Fir_S_02", sm);
        defaultHealths.put("SM_Tree_Fir_Stump_M_01", xsm);
        defaultHealths.put("SM_Tree_RedPine_S_01", xsm);
        defaultHealths.put("SM_Tree_RedPine_M_01", lg);

        noGrowth.add("SM_Tree_Fir_Stump_M_01");
        noGrowth.add("SM_Tree_Fir_Fallen_03");

        DataService.exec(() -> {
            try {
                for (GameFoliageModel foliageModel : DataService.gameFoliage.queryForAll()) {
                    //                    System.out.println("Loading Foliage #" + foliageModel.uuid);
                    foliage.put(foliageModel.uuid, foliageModel);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public ConcurrentHashMap<String, GameFoliageModel> getFoliage() {
        return foliage;
    }

    public float getStartingHealth(String name) {
        if (defaultHealths.containsKey(name)) {
            return defaultHealths.get(name);
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

            Location stumpLoc = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(f.location.cpy()).addZ(-50);
            DedicatedServer.instance.getWorld().spawn(stump, stumpLoc);

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
        // List to increase damage.
        // steel axe:8
        // iron axe:6
        // stone axe:4
        // Wood axe:1
        //punch and everything else(even weapons): 0.25 with random change of damage
        String hash = FoliageService.getHash(hitResult.getName(), hitResult.getFoliageLocation().toString());

        registerNewFoliage(hitResult);

        GameFoliageModel f = this.getFoliage(hitResult);

        float hitValue = 0.25f;
        float produces = 0;

        InventoryStack inHand = connection.getPlayer().equipmentSlots.inHand;

        if (inHand == null) {
            hitValue = 0.25f;
            produces = RandomUtil.getRandomChance(.10f) ? 1 : 0;
            connection.takeDamage(5);
        }

        if (inHand != null) {
            // Has something in their hand
            if (Hatchet.class.isAssignableFrom(inHand.getItem().getClass())) {
                Hatchet hatchet = (Hatchet) inHand.getItem();
                hitValue = hatchet.hit();
                if (SteelHatchet.class.isAssignableFrom(inHand.getItem().getClass())) {
                    produces = 1.5f;
                } else if (IronHatchet.class.isAssignableFrom(inHand.getItem().getClass())) {
                    produces = 1.4f;
                } else if (StoneHatchet.class.isAssignableFrom(inHand.getItem().getClass())) {
                    produces = 1.2f;
                } else if (WoodHatchet.class.isAssignableFrom(inHand.getItem().getClass())) {
                    produces = 1;
                } else {
                    produces = 1;
                }
            } else {
                hitValue = 0.25f;
            }
        }

        if (hitValue < 0) {
            f.cuttAtLocation = connection.getPlayer().location.cpy();
            f.syncToPlayer(connection, false);
            return;
        }

        if (hitValue > 0) {

            final float hv = hitValue;

            float oldHealth = f.health;

            if (!connection.inHandDurability(2)) {
                return;
            }

            f.health -= hv;

            connection.flashProgressBar("Tree", f.health / this.maxHealth(f), Color.RED, 5);

//            InventoryStack give = new InventoryStack(new WoodLog(), (int) produces);
//
//            final InventoryStack giveF = give;
            final GameFoliageModel ff = f;

            if (produces > 0) {
                SkillService.addExp(connection, WoodcuttingSkill.class, 2);
            }

            float finalProduces = produces;
            AnimationCallback callback = (connection1, args) -> {
//                connection.enableMovment();
                connection.showFloatingTxt("-" + hv, hitResult.getHitLocation());
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.TREE_HIT, hitResult.getHitLocation(), 5, 1f, 1f);
//                if (giveF != null && giveF.getAmount() > 0) {
//                    if (connection.getPlayer().inventory.canAdd(giveF)) {
//                        connection.getPlayer().inventory.add(giveF);
//                        connection.displayItemAdded(giveF);
//                    } else {
//                        connection.displayInventoryFull();
//                    }
//                }
                if (ff.health <= 0) {


                    // Check if this should regrow

                    if (!noGrowth.contains(f.modelName)) {
                        Stump stump = new Stump(f.uuid);
                        Location stumpLoc = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(ff.location.cpy()).addZ(-50);
                        DedicatedServer.instance.getWorld().spawn(stump, stumpLoc);

                        ff.foliageState = FoliageState.CUT;
                        ff.growth = 0.00f;
                        ff.attachedEntity = stump;

                        ff.cuttAtLocation = connection.getPlayer().location.cpy();
                        ff.syncToPlayer(connection, true);
                    } else {
                        ff.foliageState = FoliageState.CUT;
                        ff.growth = 0.00f;
                        ff.cuttAtLocation = connection.getPlayer().location.cpy();
                        ff.syncToPlayer(connection, false);
                        ff.attachedEntity = null;
                    }

//                    connection.playLocalSoundAtLocation(GameSounds.TREE_FALLING, ff.location, 1f, 1f, 3);
                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.TREE_FALLING, ff.location, 5, 1f, 1f, 4);

                    float maxHealth = getStartingHealth(ff.modelName);

                    int give = Math.max(1, Math.round(maxHealth / 7));
                    InventoryItem log = new WoodLog();

                    int bonus = 0;
                    bonus += (give * finalProduces);
                    bonus += (give * MathUtils.map(0, 99, 0, 2.5f, (float) SkillService.getLevelOfPlayer(connection, WoodcuttingSkill.class)));

                    give += bonus;

                    give = Math.round(give);

                    for (int ii = 0; ii < give; ii += 5) {
                        int gg = Math.min(give - ii, 5);
                        connection.getPlayer().inventory.add(log, gg);
                        connection.displayItemAdded(new InventoryStack(log, gg));
                    }
                    connection.updatePlayerInventory();
                    connection.syncEquipmentSlots();
                }
            };
//            connection.disableMovment();
            connection.setAnimationCallback(callback);

            if (inHand == null) {
                connection.playAnimation(Animation.PUNCH, "UpperBody", 1, 0, -1, 0.25f, 0.25f, true);
            } else {
                connection.playAnimation(Animation.SWING_AXE, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, true, true);
            }
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
