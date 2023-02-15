package com.gamefocal.island.entites.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.CapsuleShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.game.util.RandomUtil;

public class PlayerHitBox {

    private HiveNetConnection attachedToPlayer;

    private BoundingBox body = new BoundingBox();

    public PlayerHitBox(HiveNetConnection connection) {
        this.attachedToPlayer = connection;
    }

    public void drawDebug(HiveNetConnection connection) {
        BoundingBox boundingBox = this.attachedToPlayer.getBoundingBox();

        connection.drawDebugBox(
                boundingBox,
                1
        );
    }

    public NetHitResult traceMelee(Vector3 start, float range, CombatAngle angleOfAttack) {

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

    public NetHitResult traceRanged(Vector3 start, Vector3 force, float range) {
        // TODO: trace this

        return NetHitResult.NONE;
    }

}
