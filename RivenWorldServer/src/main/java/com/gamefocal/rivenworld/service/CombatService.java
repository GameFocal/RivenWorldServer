package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
import com.gamefocal.rivenworld.entites.combat.NetHitResult;
import com.gamefocal.rivenworld.entites.combat.PlayerHitBox;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
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

                        hit.playAnimation(Animation.TAKE_HIT);
                        hit.broadcastState();

                        // We found a HIT
                        System.out.println(result);
                    }
                }
            }
        }
    }

    public HiveNetConnection randedHitResult(HiveNetConnection source, Location startingLocation, Vector3 vector) {
        return null;
    }

}
