package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
import com.gamefocal.rivenworld.entites.combat.NetHitResult;
import com.gamefocal.rivenworld.entites.combat.PlayerHitBox;
import com.gamefocal.rivenworld.entites.combat.RangedProjectile;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.events.combat.PlayerDealDamageEvent;
import com.gamefocal.rivenworld.events.combat.PlayerTakeDamageEvent;
import com.gamefocal.rivenworld.game.combat.PlayerHitDamage;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.entites.projectile.ArrowProjectile;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.google.auto.service.AutoService;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.util.Comparator;
import java.util.LinkedList;
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

    public LinkedList<HiveNetConnection> getPlayersInBoundBox(BoundingBox search, HiveNetConnection source) {
        LinkedList<HiveNetConnection> found = new LinkedList<>();

        for (HiveNetConnection p : this.playerService.players.values()) {
            if (!p.getPlayer().uuid.equalsIgnoreCase(source.getPlayer().uuid)) {
                if (search.intersects(p.getBoundingBox()) || search.contains(p.getBoundingBox())) {
                    found.add(p);
                }
            }
        }

        return found;
    }

    public HiveNetConnection getClosestPlayerFromCollection(HiveNetConnection source, LinkedList<HiveNetConnection> c) {
        c.sort(new Comparator<HiveNetConnection>() {
            @Override
            public int compare(HiveNetConnection o1, HiveNetConnection o2) {

                float dst1 = o1.getPlayer().location.dist(source.getPlayer().location);
                float dst2 = o2.getPlayer().location.dist(source.getPlayer().location);

                if (dst1 < dst2) {
                    return +1;
                } else if (dst1 > dst2) {
                    return -1;
                }

                return 0;
            }
        });

        return c.getFirst();
    }


    public void meleeHitResult(HiveNetConnection source, CombatAngle attackDegree, float range) {
        Vector3 cLoc = source.getPlayer().location.toVector();
        cLoc.mulAdd(source.getForwardVector(), 50);

        BoundingBox hitZone = ShapeUtil.makeBoundBox(source.getPlayer().location.toVector(), range, 75f);

        LinkedList<HiveNetConnection> inZone = getPlayersInBoundBox(hitZone, source);
        if (inZone.size() > 0) {
            for (HiveNetConnection hit : inZone) {

                NetHitResult result = NetHitResult.NONE;

                if (!hit.getPlayer().uuid.equalsIgnoreCase(source.getPlayer().uuid)) {
                    PlayerHitBox hitBox = new PlayerHitBox(hit);
                    result = hitBox.traceMelee(source, range, attackDegree);

                    if (result != NetHitResult.NONE) {

                        // TODO: Check weapon damage type

                        PlayerHitDamage damage = new PlayerHitDamage(source, hit, 5); // TODO: Add diffrent weapon detection here

                        PlayerDealDamageEvent dealDamageEvent = new PlayerDealDamageEvent(source, 5, null, damage).call();
                        if (dealDamageEvent.isCanceled()) {
                            return;
                        }

                        PlayerTakeDamageEvent takeDamageEvent = new PlayerTakeDamageEvent(hit, damage.getDamage(), null, damage).call();
                        if (takeDamageEvent.isCanceled()) {
                            return;
                        }

                        hit.takeDamage(damage.getDamage());

//                        hit.playAnimation(Animation.TAKE_HIT);
//                        hit.broadcastState();
//
//                        // We found a HIT
////                        System.out.println(result);
//
//                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.TAKE_HIT, hit.getPlayer().location, 500, 1f, 1f);
//                        hit.getPlayer().playerStats.health -= (15 + (RandomUtil.getRandomNumberBetween(0, 5)));
                    }
                }
            }
        }
    }

    public HiveNetConnection rangedHitResult(HiveNetConnection source, Location startingLocation, float angleInDegrees, float velocity) {
//        RangedProjectile projectile = new RangedProjectile(source, angleInDegrees, velocity, startingLocation.cpy().addZ(50), source.getForwardVector(), 1500);
//        projectile.fire();

        ArrowProjectile projectile = new ArrowProjectile(2.5f,source);
        DedicatedServer.instance.getWorld().spawn(projectile, startingLocation.cpy().addZ(50).setRotation(source.getPlayer().location.getRotation()));

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
