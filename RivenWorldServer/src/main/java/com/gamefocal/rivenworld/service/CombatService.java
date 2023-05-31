package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
import com.gamefocal.rivenworld.entites.combat.CombatHitResult;
import com.gamefocal.rivenworld.entites.combat.RangedProjectile;
import com.gamefocal.rivenworld.entites.combat.hits.CombatEntityHitResult;
import com.gamefocal.rivenworld.entites.combat.hits.CombatPlayerHitResult;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.events.combat.PlayerDealDamageEvent;
import com.gamefocal.rivenworld.events.combat.PlayerTakeDamageEvent;
import com.gamefocal.rivenworld.game.DestructibleEntity;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.combat.EntityHitDamage;
import com.gamefocal.rivenworld.game.combat.PlayerDamage;
import com.gamefocal.rivenworld.game.combat.PlayerHitDamage;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.entites.projectile.ArrowProjectile;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.google.auto.service.AutoService;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Singleton
@AutoService(HiveService.class)
public class CombatService implements HiveService<CombatService> {

    @Inject
    private PlayerService playerService;

    private ConcurrentHashMap<UUID, LivingEntity> livingEntites = new ConcurrentHashMap<>();

    private ConcurrentHashMap<UUID, RangedProjectile> projectiles = new ConcurrentHashMap<>();

    @Override
    public void init() {

    }

    public void meleeHitResult(HiveNetConnection source, CombatAngle attackDegree, float range, boolean isQuickAttack) {
        InventoryStack inHand = source.getPlayer().equipmentSlots.inHand;
        float damage = 0;
        /*
         * Trace
         * */
        Vector3 start = source.getPlayer().location.toVector().add(0, 0, 65);
        Vector3 end = start.cpy().mulAdd(source.getForwardVector(), range);
        Vector3 rotateAround = new Vector3(0, 0, 1);
        if (inHand != null && ToolInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {
            damage = ((ToolInventoryItem) inHand.getItem()).hit();
        }

        if (isQuickAttack) {
            damage = Math.max(damage / 2, 5);
        }

        Vector3 cLoc = source.getPlayer().location.toVector();
        cLoc.mulAdd(source.getForwardVector(), 50);

        if (inHand != null && inHand.getItem().tagEquals("weapon", "twoHand") && !isQuickAttack) {
            start = start.cpy().mulAdd(source.getForwardVector(), 100);
        }

        float startingDeg = 0;
        float endingDeg = 180;

        Vector3 fwd = source.getForwardVector().cpy();

        Vector3 p = source.getForwardVector().cpy();
        p.rotate(rotateAround, -90);

        CombatRayHits combatHitResult = new CombatRayHits(source.getPlayer().location, source);

        /*
         * Scan for hits at diffrent angles
         * */
        while (startingDeg < endingDeg) {
            Ray r = null;
            if (attackDegree == CombatAngle.UPPER) {
                // Do the vertical trace
                float zOffset = (float) Math.tan(Math.toRadians(startingDeg));
                zOffset *= range;

                Vector3 prjEnd = end.cpy();
                prjEnd.add(new Vector3(0, 0, zOffset));

                r = new Ray(start, prjEnd.cpy().sub(start).nor());

            } else if (attackDegree == CombatAngle.FORWARD) {
                // Do a single trace
                r = new Ray(start, fwd);
            } else {
                p.rotate(rotateAround, 1);
                r = new Ray(start, p);
            }

//            source.drawDebugLine(Color.BLUE, Location.fromVector(start), Location.fromVector(start.cpy().mulAdd(r.direction, range)), 1);

            combatHitResult.get(r, range);

            startingDeg++;
        }

        combatHitResult.applyDamage(damage);
    }

    public HiveNetConnection rangedHitResult(HiveNetConnection source, Location startingLocation, float angleInDegrees, float velocity) {
        ArrowProjectile projectile = new ArrowProjectile(source, 2.5f);

        Vector3 playerRot = source.getCameraLocation().toVector();
        Vector3 crossHair = source.getCrossHairLocation().toVector();
//        crossHair.rotate(15,0,0,1);

        Vector3 forceOfArrow = crossHair.sub(playerRot).nor();

        if (source.isFirstPerson()) {
            startingLocation = Location.fromVector(startingLocation.toVector().mulAdd(forceOfArrow, 100));
        } else {
            startingLocation = Location.fromVector(startingLocation.toVector().mulAdd(forceOfArrow, 250));
        }

//        Location starting = startingLocation.cpy().setRotation(source.getPlayer().location.getRotation());
//        Vector3 fwd = starting.toVector().

        DedicatedServer.instance.getWorld().spawn(projectile, startingLocation.cpy().setRotation(source.getPlayer().location.getRotation()));
        return null;
    }

    public static class CombatRayHits {

        public Location source;
        public ArrayList<GameEntity> hitEntites = new ArrayList<>();
        public HashMap<UUID, PlayerDamage> playerDamageHashMap = new HashMap<>();
        List<GameEntity> nearByEntites = new ArrayList<>();
        private HiveNetConnection fromPlayer;

        public CombatRayHits(Location source, HiveNetConnection fromPlayer) {
            this.source = source;
            this.fromPlayer = fromPlayer;
//            nearByEntites = DedicatedServer.instance.getWorld().findCollisionEntites(source, 2500);
            nearByEntites = DedicatedServer.instance.getWorld().getCollisionManager().getNearbyEntities(this.source);
            nearByEntites.sort(((o1, o2) -> {
                float o1Z = o1.location.getZ();
                float o2Z = o2.location.getZ();

                if (o1Z > o2Z) {
                    return -1;
                } else if (o1Z < o2Z) {
                    return +1;
                }

                return 0;

            }));
        }

        public CombatRayHits get(Ray r, float range) {

            /*
             * Check Entites
             * */
            ArrayList<GameEntity> entities = new ArrayList<>();
            for (GameEntity e : nearByEntites) {
                if (CollisionEntity.class.isAssignableFrom(e.getClass()) || LivingEntity.class.isAssignableFrom(e.getClass())) {
                    if (Intersector.intersectRayBoundsFast(r, e.getBoundingBox())) {
                        if (e.location.dist(source) <= range) {
                            this.hitEntites.add(e);
                        }
                    }
                }
            }

            /*
             * Players
             * */
            for (HiveNetConnection hit : DedicatedServer.get(PlayerService.class).players.values()) {
                if (!fromPlayer.getPlayer().uuid.equalsIgnoreCase(hit.getPlayer().uuid)) {
                    if (hit.getPlayer().location.dist(source) <= range) {

                        Vector3 feet = hit.getPlayer().location.toVector();

                        if (hit.getState().blendState.IsCrouching) {
                            feet.z -= 40;
                        }

                        BoundingBox legs = ShapeUtil.makeBoundBox(feet.cpy().add(0, 0, -50), 15, 40);
                        BoundingBox body = ShapeUtil.makeBoundBox(feet.cpy().add(0, 0, 30), 15, 40);
                        BoundingBox head = ShapeUtil.makeBoundBox(feet.cpy().add(0, 0, 80), 15, 10);

                        int headHits = 0;
                        int bodyHits = 0;
                        int legHits = 0;

                        if (Intersector.intersectRayBoundsFast(r, legs)) {
                            if (hit.getPlayer().location.dist(fromPlayer.getPlayer().location) <= range) {
                                legHits++;
                            }
                        }
                        if (Intersector.intersectRayBoundsFast(r, body)) {
                            if (hit.getPlayer().location.dist(fromPlayer.getPlayer().location) <= range) {
                                bodyHits++;
                            }
                        }
                        if (Intersector.intersectRayBoundsFast(r, head)) {
                            if (hit.getPlayer().location.dist(fromPlayer.getPlayer().location) <= range) {
                                headHits++;
                            }
                        }


                        if (legHits > 0 || bodyHits > 0 || headHits > 0) {

                            PlayerDamage pd = new PlayerDamage();
                            pd.player = hit.getUuid();
                            pd.totalTraces = 0;
                            if (this.playerDamageHashMap.containsKey(hit.getUuid())) {
                                pd = this.playerDamageHashMap.get(hit.getUuid());
                            }

                            pd.headHits += headHits;
                            pd.bodyHits += bodyHits;
                            pd.legHits += legHits;
                            pd.totalTraces++;

                            this.playerDamageHashMap.put(pd.player, pd);
                        }
                    }
                }
            }

            return this;
        }

        public CombatHitResult applyDamage(float damage) {

            InventoryStack inHand = fromPlayer.getPlayer().equipmentSlots.inHand;

            if (this.hitEntites.size() > 0) {
                this.hitEntites = (ArrayList<GameEntity>) this.hitEntites.stream().filter(GameEntity::isHasCollision).collect(Collectors.toList());
                this.hitEntites.sort((o1, o2) -> {
                    float o1Dist = fromPlayer.getPlayer().location.dist(o1.location);
                    float o2Dist = fromPlayer.getPlayer().location.dist(o2.location);
                    if (o1Dist > o2Dist) {
                        return +1;
                    } else if (o1Dist < o2Dist) {
                        return -1;
                    } else {
                        return 0;
                    }
                });
                this.hitEntites.sort((o1, o2) -> {
                    float o1Dist = o1.location.getZ();
                    float o2Dist = o2.location.getZ();

                    if (o1Dist < o2Dist) {
                        return +1;
                    } else if (o1Dist > o2Dist) {
                        return -1;
                    } else {
                        return 0;
                    }
                });
                GameEntity e = this.hitEntites.get(0);

//            source.drawDebugBox(((CollisionEntity) e).collisionBox(), 1);

                if (damage > 0) {

                    if (DestructibleEntity.class.isAssignableFrom(e.getClass())) {
                        /*
                         * Hit Distrutable Entity
                         * */

                        DestructibleEntity d = (DestructibleEntity) e;

                        float multi = d.getDamageValueMultiple(inHand.getItem());

                        float durabilityUse = 5;
//                        if (multi < 1) {
//                            durabilityUse = 20;
//                        }

                        damage *= multi;

                        PlayerDealDamageEvent dealDamageEvent = new PlayerDealDamageEvent(fromPlayer, damage, null, new EntityHitDamage(fromPlayer, e, damage)).call();
                        if (!dealDamageEvent.isCanceled()) {

                            damage = dealDamageEvent.getDamage();

                            // Hit the entity
                            ((CollisionEntity) e).takeDamage(damage);
                            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.HitPlacable, e.location, 2500, 1, 1);

                            // Play Sound
                            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.HitPlacable, e.location, 2500, 1, 1);

                            // Use durability
                            inHand.getItem().useDurability(durabilityUse);
                            if (inHand.getItem().getDurability() <= 0) {
                                fromPlayer.breakItemInSlot(EquipmentSlot.PRIMARY);
                            }

                            if (d.getHealth() <= 0) {
                                // Break the item
                                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PlacableBreak, e.location, 2500, 1, 1);
                                DedicatedServer.instance.getWorld().despawn(d.uuid);
                            }

                            fromPlayer.showFloatingTxt("-" + damage, e.location.cpy().addZ(50));

                            fromPlayer.flashProgressBar(e.getRelatedItem().getName(), ((DestructibleEntity<?>) e).getHealth() / ((DestructibleEntity<?>) e).getMaxHealth(), Color.RED, 5);

                            fromPlayer.updatePlayerInventory();

                            /*
                             * Trigger combat
                             * */
                            e.getChunk().markInCombat();

                            return new CombatEntityHitResult(fromPlayer, e, new Vector3());
//                            fromPlayer.syncEquipmentSlots();
                        }
                    } else if (LivingEntity.class.isAssignableFrom(e.getClass())) {
                        /*
                         * Hit Living Entity, do damage
                         * */

                        LivingEntity livingEntity = (LivingEntity) e;

                        if (!livingEntity.canBeDamaged) {
                            return null;
                        }

                        PlayerDealDamageEvent dealDamageEvent = new PlayerDealDamageEvent(fromPlayer, damage, null, new EntityHitDamage(fromPlayer, e, damage));
                        if (!dealDamageEvent.isCanceled()) {

                            damage = dealDamageEvent.getDamage();

                            livingEntity.health -= damage;

                            if (livingEntity.health <= 0) {
                                livingEntity.kill();
                            }

                            livingEntity.lastAttackedBy = fromPlayer;
                            livingEntity.lastAttacked = System.currentTimeMillis();
                            livingEntity.attackResponse = true;

                            fromPlayer.showFloatingTxt("-" + damage, e.location.cpy().addZ(50));

                            fromPlayer.flashProgressBar(e.getClass().getSimpleName(), ((LivingEntity<?>) e).getHealth() / ((LivingEntity<?>) e).getMaxHealth(), Color.RED, 5);

                            fromPlayer.updatePlayerInventory();

                            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.HIT_FLESH, livingEntity.location, 1500, 1, 1);

                            /*
                             * Trigger combat
                             * */
                            e.getChunk().markInCombat();

                            return new CombatEntityHitResult(fromPlayer, e, new Vector3());
                        }

                    }

                }
            }

            if (this.playerDamageHashMap.size() > 0) {
                // A player was hit... we need to process the hits

                for (PlayerDamage pd : this.playerDamageHashMap.values()) {
                    HiveNetConnection hit = DedicatedServer.get(PlayerService.class).players.get(pd.player);
                    if (hit != null) {

                        /*
                         * Take Damage
                         * */
                        PlayerHitDamage damageHit = new PlayerHitDamage(fromPlayer, hit, damage);

                        PlayerDealDamageEvent dealDamageEvent = new PlayerDealDamageEvent(fromPlayer, 5, null, damageHit).call();
                        if (dealDamageEvent.isCanceled()) {
                            return null;
                        }

                        PlayerTakeDamageEvent takeDamageEvent = new PlayerTakeDamageEvent(hit, damageHit.getDamage(), null, damageHit).call();
                        if (takeDamageEvent.isCanceled()) {
                            return null;
                        }

                        float hitVal = 0;

                        if (pd.headHits > 0) {
                            if (hit.getPlayer().equipmentSlots.head != null) {

                                hitVal = damage * ((float) pd.headHits / (float) pd.totalTraces);

//                                System.out.println("HEAD: " + hitVal);

                                InventoryStack headGear = hit.getPlayer().equipmentSlots.head;
                                // apply damage (hitval) to item
                                headGear.getItem().useDurability(hitVal);

                                if (headGear.getItem().getDurability() <= 0) {
                                    // Break the helment
                                    hit.breakItemInSlot(EquipmentSlot.HEAD);
                                }
                                // this is reducing damage base of item durability, need to change to a % of hit.
                                damage -= headGear.getItem().getDurability();
                            }
                        }

                        if (pd.bodyHits > 0) {
                            if (hit.getPlayer().equipmentSlots.chest != null) {
//                                System.out.println("damage pass to body->"+damage);
//                                System.out.println("damage body hit->"+pd.bodyHits);
//                                System.out.println("damage total trace->"+pd.totalTraces);

                                hitVal = damage * ((float) pd.bodyHits / (float) pd.totalTraces);

//                                System.out.println("BODY: " + hitVal);

                                InventoryStack headGear = hit.getPlayer().equipmentSlots.chest;
                                headGear.getItem().useDurability(hitVal);

                                if (headGear.getItem().getDurability() <= 0) {
                                    // Break the helment
                                    hit.breakItemInSlot(EquipmentSlot.BODY);
                                }
                                // this is reducing damage base of item durability, need to change to a % of hit.
                                damage -= headGear.getItem().getDurability();
                            }
                        }

                        if (pd.legHits > 0) {
                            if (hit.getPlayer().equipmentSlots.legs != null) {

                                hitVal = damage * ((float) pd.legHits / (float) pd.totalTraces);

//                                System.out.println("LEGS: " + hitVal);

                                InventoryStack headGear = hit.getPlayer().equipmentSlots.legs;
                                headGear.getItem().useDurability(hitVal);

                                if (headGear.getItem().getDurability() <= 0) {
                                    // Break the helment
                                    hit.breakItemInSlot(EquipmentSlot.LEGS);
                                }
                                // this is reducing damage base of item durability, need to change to a % of hit.
                                damage -= headGear.getItem().getDurability();
                            }
                        }

                        if (damage <= 0) {
                            damage = 2;
                        }
                        // Change damage applied to nerf damage after taking into account reduce durability of item.
                        hit.takeDamage(damageHit.getDamage());
                        hit.disableMovment();
                        hit.StopAnimations();
                        TaskService.scheduledDelayTask(hit::enableMovment, TickUtil.MILLISECONDS(500),false);
                        System.out.println("damage apply to player" + damageHit.getDamage());

                        fromPlayer.showFloatingTxt("-" + damageHit.getDamage(), hit.getPlayer().location.cpy().addZ(150));

                        return new CombatPlayerHitResult(fromPlayer, hit, new Vector3());
                    }
                }

            }

            return null;
        }
    }

}
