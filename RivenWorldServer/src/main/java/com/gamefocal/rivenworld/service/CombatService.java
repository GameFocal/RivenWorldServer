package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
import com.gamefocal.rivenworld.entites.combat.CombatHitResult;
import com.gamefocal.rivenworld.entites.combat.RangedProjectile;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.events.combat.PlayerDealDamageEvent;
import com.gamefocal.rivenworld.events.combat.PlayerTakeDamageEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.combat.PlayerDamage;
import com.gamefocal.rivenworld.game.combat.PlayerHitDamage;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.entites.projectile.ArrowProjectile;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.google.auto.service.AutoService;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
        if (inHand != null && ToolInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {
            damage = ((ToolInventoryItem) inHand.getItem()).hit();
        }

        if (isQuickAttack) {
            damage = Math.max(damage / 2, 5);
        }

        Vector3 cLoc = source.getPlayer().location.toVector();
        cLoc.mulAdd(source.getForwardVector(), 50);

        /*
         * Trace
         * */
        Vector3 start = source.getPlayer().location.toVector().add(0, 0, 65);
        Vector3 end = start.cpy().mulAdd(source.getForwardVector(), range);

        Vector3 rotateAround = new Vector3(0, 0, 1);

        float startingDeg = 0;
        float endingDeg = 180;

        Vector3 fwd = source.getForwardVector().cpy();

        Vector3 p = source.getForwardVector().cpy();
        p.rotate(rotateAround, -90);

        CombatRayHits combatHitResult = new CombatRayHits(source.getPlayer().location,source);

        float totalTraces = Math.abs(endingDeg - startingDeg);
        while (startingDeg < endingDeg) {
            totalTraces++;

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

            source.drawDebugLine(Location.fromVector(start), Location.fromVector(start.cpy().mulAdd(r.direction, range)), 1);

            combatHitResult.get(r, range);

            startingDeg++;
        }

        if (combatHitResult.hitEntites.size() > 0) {
            combatHitResult.hitEntites.sort((o1, o2) -> {
                float o1Dist = source.getPlayer().location.dist(o1.location);
                float o2Dist = source.getPlayer().location.dist(o2.location);

                if (o1Dist > o2Dist) {
                    return +1;
                } else if (o1Dist < o2Dist) {
                    return -1;
                } else {
                    return 0;
                }
            });
            GameEntity e = combatHitResult.hitEntites.get(0);

            source.drawDebugBox(((CollisionEntity) e).collisionBox(), 1);

            // Hit the entity
            ((CollisionEntity) e).takeDamage(damage);
            // Use durability
            return;
        }

        if (combatHitResult.playerDamageHashMap.size() > 0) {
            // A player was hit... we need to process the hits

            for (PlayerDamage pd : combatHitResult.playerDamageHashMap.values()) {
                HiveNetConnection hit = DedicatedServer.get(PlayerService.class).players.get(pd.player);
                if (hit != null) {

                    float hitVal = 0;

                    if (pd.headHits > 0) {
                        if (hit.getPlayer().equipmentSlots.head != null) {

                            hitVal = damage * ((float) pd.headHits / (float) pd.totalTraces);

                            System.out.println("HEAD: " + hitVal);

                            InventoryStack headGear = hit.getPlayer().equipmentSlots.head;
                            headGear.getItem().useDurability(hitVal);

                            if (headGear.getItem().getDurability() <= 0) {
                                // Break the helment
                                hit.breakItemInSlot(EquipmentSlot.HEAD);
                            }

                            damage -= headGear.getItem().getDurability();
                        }
                    }

                    if (pd.bodyHits > 0) {
                        if (hit.getPlayer().equipmentSlots.chest != null) {

                            hitVal = damage * ((float) pd.bodyHits / (float) pd.totalTraces);

                            System.out.println("BODY: " + hitVal);

                            InventoryStack headGear = hit.getPlayer().equipmentSlots.chest;
                            headGear.getItem().useDurability(hitVal);

                            if (headGear.getItem().getDurability() <= 0) {
                                // Break the helment
                                hit.breakItemInSlot(EquipmentSlot.BODY);
                            }

                            damage -= headGear.getItem().getDurability();
                        }
                    }

                    if (pd.legHits > 0) {
                        if (hit.getPlayer().equipmentSlots.legs != null) {

                            hitVal = damage * ((float) pd.legHits / (float) pd.totalTraces);

                            System.out.println("LEGS: " + hitVal);

                            InventoryStack headGear = hit.getPlayer().equipmentSlots.legs;
                            headGear.getItem().useDurability(hitVal);

                            if (headGear.getItem().getDurability() <= 0) {
                                // Break the helment
                                hit.breakItemInSlot(EquipmentSlot.LEGS);
                            }

                            damage -= headGear.getItem().getDurability();
                        }
                    }

                    if (damage <= 0) {
                        damage = 2;
                    }

                    /*
                     * Take Damage
                     * */
                    PlayerHitDamage damageHit = new PlayerHitDamage(source, hit, damage); // TODO: Add diffrent weapon detection here

                    PlayerDealDamageEvent dealDamageEvent = new PlayerDealDamageEvent(source, 5, null, damageHit).call();
                    if (dealDamageEvent.isCanceled()) {
                        return;
                    }

                    PlayerTakeDamageEvent takeDamageEvent = new PlayerTakeDamageEvent(hit, damageHit.getDamage(), null, damageHit).call();
                    if (takeDamageEvent.isCanceled()) {
                        return;
                    }

                    hit.takeDamage(damageHit.getDamage());
                }
            }

        }

    }

    public HiveNetConnection rangedHitResult(HiveNetConnection source, Location startingLocation, float angleInDegrees, float velocity) {
        ArrowProjectile projectile = new ArrowProjectile(source, 2.5f);
        DedicatedServer.instance.getWorld().spawn(projectile, startingLocation.cpy().addZ(75).setRotation(source.getPlayer().location.getRotation()));
        return null;
    }

    public static class CombatRayHits {

        public Location source;
        private HiveNetConnection fromPlayer;

        List<GameEntity> nearByEntites = new ArrayList<>();
        public ArrayList<GameEntity> hitEntites = new ArrayList<>();
        public HashMap<UUID, PlayerDamage> playerDamageHashMap = new HashMap<>();

        public CombatRayHits(Location source, HiveNetConnection fromPlayer) {
            this.source = source;
            this.fromPlayer = fromPlayer;
            nearByEntites = DedicatedServer.instance.getWorld().findCollisionEntites(source, 2500);
        }

        public CombatRayHits get(Ray r, float range) {

            /*
             * Check Entites
             * */
            ArrayList<GameEntity> entities = new ArrayList<>();
            for (GameEntity e : nearByEntites) {
                if (CollisionEntity.class.isAssignableFrom(e.getClass())) {
                    if (Intersector.intersectRayBoundsFast(r, ((CollisionEntity) e).collisionBox())) {
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
                            if (this.playerDamageHashMap.containsKey(hit.getUuid())) {
                                pd = this.playerDamageHashMap.get(hit.getUuid());
                            }

                            pd.headHits += headHits;
                            pd.bodyHits += bodyHits;
                            pd.legHits += legHits;

                            this.playerDamageHashMap.put(pd.player, pd);
                        }
                    }
                }
            }

            return this;
        }
    }

}
