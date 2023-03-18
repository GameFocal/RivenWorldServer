package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
import com.gamefocal.rivenworld.entites.combat.NetHitResult;
import com.gamefocal.rivenworld.entites.combat.PlayerHitBox;
import com.gamefocal.rivenworld.entites.combat.RangedProjectile;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.google.auto.service.AutoService;
import com.google.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;

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
                        hit.takeDamage(5);

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

    public HiveNetConnection randedHitResult(HiveNetConnection source, Location startingLocation, float angleInDegrees, float velocity) {
        RangedProjectile projectile = new RangedProjectile(angleInDegrees, velocity, startingLocation.cpy().addZ(50), source.getForwardVector(), 1500);
        projectile.fire();
        this.projectiles.put(projectile.getUuid(), projectile);
        return null;
    }

    public void trackProjectiles() {
        for (RangedProjectile projectile : this.projectiles.values()) {
            if (projectile.isDead()) {
                System.out.println("DEAD PROJECTILE");
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

                        // TODO: Check arrow type vs armor type
                        connection.takeDamage(5);

//                        for (HiveNetConnection connection1 : DedicatedServer.get(PlayerService.class).players.values()) {
//                            connection1.drawDebugLine(Location.fromVector(r.origin), Location.fromVector(hit), 2);
//                        }
                    }
                }
            }

            // TODO: Check animals

        }
    }

}
