package com.gamefocal.island.service;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.combat.CombatAngle;
import com.gamefocal.island.entites.combat.NetHitResult;
import com.gamefocal.island.entites.combat.PlayerHitBox;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.game.util.ShapeUtil;
import com.google.auto.service.AutoService;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.util.*;

@Singleton
@AutoService(HiveService.class)
public class CombatService implements HiveService<CombatService> {

    @Inject
    private PlayerService playerService;

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


    public HiveNetConnection meleeHitResult(HiveNetConnection source, CombatAngle attackDegree, float range) {

        // Search players nearby
        for (HiveNetConnection target : DedicatedServer.get(PlayerService.class).players.values()) {
//            if (source.getPlayer().location.dist(target.getPlayer().location) <= 500 && !target.getPlayer().uuid.equalsIgnoreCase(source.getPlayer().uuid)) {
            // Check hitbox here.

//            PlayerHitBox hitBox = new PlayerHitBox(target);
//            hitBox.drawDebug(source);

            Vector3 cLoc = source.getPlayer().location.toVector();
            cLoc.mulAdd(source.getForwardVector(), 50);

            BoundingBox hitZone = ShapeUtil.makeBoundBox(cLoc, 30f, 75f);
//            source.drawDebugBox(hit, 2);

            NetHitResult result = NetHitResult.NONE;

            LinkedList<HiveNetConnection> inZone = getPlayersInBoundBox(hitZone, source);
            if (inZone.size() > 0) {
                // Has something
                HiveNetConnection hit = getClosestPlayerFromCollection(source, inZone);
                if (hit != null) {
                    PlayerHitBox hitBox = new PlayerHitBox(target);
                    result = hitBox.traceMelee(source.getPlayer().location.toVector(), range, attackDegree);
                }
            }

            if (result != NetHitResult.NONE) {
                // We found a HIT



            }
        }

        return null;
    }

    public HiveNetConnection randedHitResult(HiveNetConnection source, Location startingLocation, Vector3 vector) {
        return null;
    }

}
