package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
import com.gamefocal.rivenworld.entites.combat.NetHitResult;
import com.gamefocal.rivenworld.entites.combat.PlayerHitBox;
import com.gamefocal.rivenworld.entites.combat.RangedProjectile;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.events.combat.PlayerDealDamageEvent;
import com.gamefocal.rivenworld.events.combat.PlayerTakeDamageEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.combat.PlayerHitDamage;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.entites.projectile.ArrowProjectile;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.game.util.VectorUtil;
import com.google.auto.service.AutoService;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.util.*;
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
        Vector3 cLoc = source.getPlayer().location.toVector();
        cLoc.mulAdd(source.getForwardVector(), 50);

        /*
         * Trace
         * */
        Vector3 start = source.getPlayer().location.toVector();
        Vector3 forward = start.cpy().mulAdd(source.getForwardVector(), range);

        float deg = (float) VectorUtil.getDegrees(start, forward);

        float startingDeg = (deg - 90);
        float endingDeg = (deg - 90) + 180;

        if (attackDegree == CombatAngle.FORWARD || attackDegree == CombatAngle.UPPER) {
            startingDeg = (deg - 90) + 75;
            endingDeg = (deg - 90) + 115;
        }

        List<GameEntity> nearByEntites = DedicatedServer.instance.getWorld().findCollisionEntites(source.getPlayer().location, 2500);

        while (startingDeg < endingDeg) {
            Vector3 n = VectorUtil.calculateOrbit(startingDeg, range, start, forward, start.z);

            Vector3 dir = n.cpy().sub(start);
            dir.nor();

            Ray r = new Ray(start, dir);

            source.drawDebugLine(Location.fromVector(r.origin),Location.fromVector(start.cpy().mulAdd(dir,100)),1);

            boolean hitEntity = false;

            /*
             * Check Entites
             * */
            for (GameEntity e : nearByEntites) {
                if (CollisionEntity.class.isAssignableFrom(e.getClass())) {
                    if (Intersector.intersectRayBoundsFast(r, ((CollisionEntity) e).collisionBox())) {

                        source.drawDebugBox(((CollisionEntity) e).collisionBox(), 1);

                        System.out.println("HIT BOX");

                        // Hit the entity
                        ((CollisionEntity) e).takeDamage(0);
                        hitEntity = true;
                    }
                }
            }

            /*
             * Players
             * */
            if (!hitEntity) {
                for (HiveNetConnection hit : DedicatedServer.get(PlayerService.class).players.values()) {
                    if (!source.getPlayer().uuid.equalsIgnoreCase(hit.getPlayer().uuid)) {

                        Vector3 feet = hit.getPlayer().location.toVector();

                        BoundingBox legs = ShapeUtil.makeBoundBox(feet.cpy().add(0, 0, -50), 15, 40);
                        BoundingBox body = ShapeUtil.makeBoundBox(feet.cpy().add(0, 0, 30), 15, 40);
                        BoundingBox head = ShapeUtil.makeBoundBox(feet.cpy().add(0, 0, 80), 15, 10);

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

                        /*
                         * See what they hit
                         * */
                        if (headHits > 0) {
                            System.out.println("HEAD HIT: " + headHits);
                        }

                        if (bodyHits > 0) {
                            System.out.println("BODY HIT: " + bodyHits);
                        }

                        if (legHits > 0) {
                            System.out.println("LEG HIT: " + legHits);
                        }
                    }
                }
            }

            startingDeg++;
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
