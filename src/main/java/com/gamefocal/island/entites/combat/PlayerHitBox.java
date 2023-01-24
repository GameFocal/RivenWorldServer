package com.gamefocal.island.entites.combat;

import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.island.entites.net.HiveNetConnection;

public class PlayerHitBox {

    private float totalHeightInMeters = 200;

    private BoundingBox body;

    private BoundingBox neck;

    private Sphere head;

    public PlayerHitBox(HiveNetConnection connection) {

        Vector3 floorPosition = connection.getPlayer().location.toVector();
        Vector3 upperBody = floorPosition.cpy().add(0, 0, 125);
        Vector3 neck = upperBody.cpy().add(0, 0, 25).cpy();
        Vector3 head = neck.cpy().add(0, 0, 25);

        this.body = new BoundingBox(floorPosition, upperBody);
        this.neck = new BoundingBox(upperBody, neck);
        this.head = new Sphere(head, 25);
    }

    public NetHitResult traceMelee(Vector3 start, float range, float angleOfAttack) {
        // TODO: Trace this

        return NetHitResult.NONE;
    }

    public NetHitResult traceRanged(Vector3 start, Vector3 force, float range) {
        // TODO: trace this

        return NetHitResult.NONE;
    }

}
