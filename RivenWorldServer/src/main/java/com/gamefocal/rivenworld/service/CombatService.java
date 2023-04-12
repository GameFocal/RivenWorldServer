package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
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
import com.gamefocal.rivenworld.game.util.VectorUtil;
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

    public void meleeHitResult(HiveNetConnection source, CombatAngle attackDegree, float range) {

        HashMap<UUID, PlayerDamage> playerDamageHashMap = new HashMap<>();

        InventoryStack inHand = source.getPlayer().equipmentSlots.inHand;
        float damage = 0;
        if (inHand != null && ToolInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {
            damage = ((ToolInventoryItem) inHand.getItem()).hit();
        }

        Vector3 cLoc = source.getPlayer().location.toVector();
        cLoc.mulAdd(source.getForwardVector(), 50);

        /*
         * Trace
         * */
        Vector3 start = source.getPlayer().location.toVector().add(0, 0, 65);
        Vector3 forward = start.cpy().mulAdd(source.getForwardVector(), range);

        float deg = (float) VectorUtil.getDegrees(start, forward);

        float startingDeg = (deg - 90);
        float endingDeg = (deg - 90) + 180;

        if (attackDegree == CombatAngle.FORWARD || attackDegree == CombatAngle.UPPER) {
            startingDeg = (deg - 90) + 75;
            endingDeg = (deg - 90) + 115;
        }

        ArrayList<GameEntity> hitEntites = new ArrayList<>();
        List<GameEntity> nearByEntites = DedicatedServer.instance.getWorld().findCollisionEntites(source.getPlayer().location, 2500);

        float totalTraces = Math.abs(endingDeg - startingDeg);
        while (startingDeg < endingDeg) {
            totalTraces++;
            Vector3 n = VectorUtil.calculateOrbit(startingDeg, range, start, forward, start.z);
            n.z = forward.z;

            Vector3 dir = n.cpy().sub(start);
            dir.nor();

            Ray r = new Ray(start, dir);

            source.drawDebugLine(Location.fromVector(r.origin), Location.fromVector(start.cpy().mulAdd(dir, 100)), 1);

            boolean hitEntity = false;

            /*
             * Check Entites
             * */
            ArrayList<GameEntity> entities = new ArrayList<>();
            for (GameEntity e : nearByEntites) {
                if (CollisionEntity.class.isAssignableFrom(e.getClass())) {
                    if (Intersector.intersectRayBoundsFast(r, ((CollisionEntity) e).collisionBox())) {
//                        if (e.location.dist(source.getPlayer().location) <= 500) {
//                        source.drawDebugBox(((CollisionEntity) e).collisionBox(), 1);
//
//                        // Hit the entity
//                        ((CollisionEntity) e).takeDamage(0);
//                        hitEntity = true;
                        hitEntites.add(e);
//                        }
                    }
                }
            }

            /*
             * Players
             * */
            for (HiveNetConnection hit : DedicatedServer.get(PlayerService.class).players.values()) {
                if (!source.getPlayer().uuid.equalsIgnoreCase(hit.getPlayer().uuid)) {

                    Vector3 feet = hit.getPlayer().location.toVector();

                    if (hit.getState().blendState.IsCrouching) {
                        feet.z -= 40;
                    }

                    BoundingBox legs = ShapeUtil.makeBoundBox(feet.cpy().add(0, 0, -50), 15, 40);
                    BoundingBox body = ShapeUtil.makeBoundBox(feet.cpy().add(0, 0, 30), 15, 40);
                    BoundingBox head = ShapeUtil.makeBoundBox(feet.cpy().add(0, 0, 80), 15, 10);

                    source.drawDebugBox(head, 1);
                    source.drawDebugBox(body, 1);
                    source.drawDebugBox(legs, 1);

                    int headHits = 0;
                    int bodyHits = 0;
                    int legHits = 0;

                    if (Intersector.intersectRayBoundsFast(r, legs)) {
                        if (hit.getPlayer().location.dist(source.getPlayer().location) <= range) {
                            legHits++;
                        }
                    }
                    if (Intersector.intersectRayBoundsFast(r, body)) {
                        if (hit.getPlayer().location.dist(source.getPlayer().location) <= range) {
                            bodyHits++;
                        }
                    }
                    if (Intersector.intersectRayBoundsFast(r, head)) {
                        if (hit.getPlayer().location.dist(source.getPlayer().location) <= range) {
                            headHits++;
                        }
                    }


                    if (legHits > 0 || bodyHits > 0 || headHits > 0) {

                        PlayerDamage pd = new PlayerDamage();
                        pd.totalTraces = (int) totalTraces;
                        pd.player = hit.getUuid();
                        if (playerDamageHashMap.containsKey(hit.getUuid())) {
                            pd = playerDamageHashMap.get(hit.getUuid());
                        }

                        pd.headHits += headHits;
                        pd.bodyHits += bodyHits;
                        pd.legHits += legHits;

                        playerDamageHashMap.put(pd.player, pd);
                    }
                }
            }

            startingDeg++;
        }

        if (hitEntites.size() > 0) {
            hitEntites.sort((o1, o2) -> {
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
            GameEntity e = hitEntites.get(0);

            source.drawDebugBox(((CollisionEntity) e).collisionBox(), 1);

            // Hit the entity
            ((CollisionEntity) e).takeDamage(0);
            return;
        }

        if (playerDamageHashMap.size() > 0) {
            // A player was hit... we need to process the hits

            for (PlayerDamage pd : playerDamageHashMap.values()) {
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
//        RangedProjectile projectile = new RangedProjectile(source, angleInDegrees, velocity, startingLocation.cpy().addZ(50), source.getForwardVector(), 1500);
//        projectile.fire();

        ArrowProjectile projectile = new ArrowProjectile(source, 2.5f);
        DedicatedServer.instance.getWorld().spawn(projectile, startingLocation.cpy().addZ(75).setRotation(source.getPlayer().location.getRotation()));

//        this.projectiles.put(projectile.getUuid(), projectile);
        return null;
    }

    public void trackProjectiles() {
/*
        for (RangedProjectile projectile : this.projectiles.values()) {
            if (projectile.isDead()) {
                this.projectiles.remove(projectile.getUuid());
                continue;
            }

            Ray r = projectile.getProjectedSpace();

            // Check players
            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {

                Vector3 hit = new Vector3();

                if (Intersector.intersectRayBounds(r, connection.getBoundingBox(), hit)) {
                    // Check distance if it is like a arrow
                    if (hit.dst(r.origin) <= 50) {
                        System.out.println("HIT");

                        PlayerHitDamage damage = new PlayerHitDamage(projectile.getSource(), connection, 5); // TODO: Add diffrent weapon detection here

                        PlayerDealDamageEvent dealDamageEvent = new PlayerDealDamageEvent(projectile.getSource(), 5, null, damage).call();
                        if (dealDamageEvent.isCanceled()) {
                            return;
                        }

                        PlayerTakeDamageEvent takeDamageEvent = new PlayerTakeDamageEvent(connection, damage.getDamage(), null, damage).call();
                        if (takeDamageEvent.isCanceled()) {
                            return;
                        }

                        // TODO: Check arrow type vs armor type
                        connection.takeDamage(damage.getDamage());

//                        for (HiveNetConnection connection1 : DedicatedServer.get(PlayerService.class).players.values()) {
//                            connection1.drawDebugLine(Location.fromVector(r.origin), Location.fromVector(hit), 2);
//                        }
                    }
                }
            }

            // TODO: Check animals

        }
*/
    }

}
