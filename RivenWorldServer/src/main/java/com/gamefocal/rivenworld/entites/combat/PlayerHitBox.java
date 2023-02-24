package com.gamefocal.rivenworld.entites.combat;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.VectorUtil;

public class PlayerHitBox {

    private HiveNetConnection attachedToPlayer;

    private BoundingBox hitBox = new BoundingBox();

    public PlayerHitBox(HiveNetConnection connection) {
        this.attachedToPlayer = connection;
        this.hitBox = this.attachedToPlayer.getBoundingBox();
    }

    public void drawDebug(HiveNetConnection connection) {
        BoundingBox boundingBox = this.attachedToPlayer.getBoundingBox();

        connection.drawDebugBox(
                boundingBox,
                1
        );
    }

    public NetHitResult traceMelee(HiveNetConnection source, float range, CombatAngle angleOfAttack) {

        Vector3 start = source.getPlayer().location.toVector();
        Vector3 forward = start.cpy().mulAdd(source.getForwardVector(), range);

        float deg = VectorUtil.getDegrees(start, forward);

        float startingDeg = deg;
        float endingDeg = deg + 180;

        if (angleOfAttack == CombatAngle.FORWARD || angleOfAttack == CombatAngle.UPPER) {
            startingDeg = deg + 75;
            endingDeg = deg + 115;
        }

        int hits = 0;
        while (startingDeg < endingDeg) {
            Vector3 n = VectorUtil.calculateOrbit(startingDeg, range, start, forward, start.z);
//            this.attachedToPlayer.drawDebugLine(Location.fromVector(start), Location.fromVector(n), 1);

            Vector3 dir = n.cpy().sub(start);
            dir.nor();

            Ray r = new Ray(start, dir);

            if (Intersector.intersectRayBoundsFast(r, this.hitBox)) {
                if (this.attachedToPlayer.getPlayer().location.dist(source.getPlayer().location) <= range) {
                    hits++;
//                    source.drawDebugLine(Location.fromVector(start), Location.fromVector(r.getEndPoint(new Vector3(), range)), 1);
                }
            }

            startingDeg++;
        }

        if (hits > 0) {

            System.out.println(hits + " total hits");

            CombatStance stance = CombatStance.getFromIndex(this.attachedToPlayer.getState().blendState.attackMode);
            CombatAngle angle = CombatAngle.getFromIndex(this.attachedToPlayer.getState().blendState.attackDirection);

            if (stance == CombatStance.BLOCK) {
                // See if they are blocking the same side
                if (angleOfAttack == CombatAngle.FORWARD && angle == CombatAngle.FORWARD) {
                    return NetHitResult.BLOCK;
                }
                if (angleOfAttack == CombatAngle.UPPER && angle == CombatAngle.UPPER) {
                    return NetHitResult.BLOCK;
                }
                if (angleOfAttack == CombatAngle.LEFT && angle == CombatAngle.RIGHT) {
                    return NetHitResult.BLOCK;
                }
                if (angleOfAttack == CombatAngle.RIGHT && angle == CombatAngle.LEFT) {
                    return NetHitResult.BLOCK;
                }
            }

            if (RandomUtil.getRandomChance(5)) {
                return NetHitResult.CRITICAL_HIT;
            }

            return NetHitResult.HIT;
        }

        return NetHitResult.NONE;
    }

    public NetHitResult traceRanged(Vector3 start, Vector3 force, float range) {
        // TODO: trace this

        return NetHitResult.NONE;
    }

}
