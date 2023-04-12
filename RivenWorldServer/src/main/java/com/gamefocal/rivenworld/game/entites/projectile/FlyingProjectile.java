package com.gamefocal.rivenworld.game.entites.projectile;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatHitResult;
import com.gamefocal.rivenworld.entites.combat.hits.CombatEntityHitResult;
import com.gamefocal.rivenworld.entites.combat.hits.CombatPlayerHitResult;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.entites.generics.DisposableEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.ray.RayRequestCallback;
import com.gamefocal.rivenworld.game.ray.UnrealTerrainRayRequest;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.VectorUtil;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.PlayerService;
import com.gamefocal.rivenworld.service.RayService;

import java.util.concurrent.TimeUnit;

public abstract class FlyingProjectile<T> extends GameEntity<T> implements TickEntity, DisposableEntity {

    protected float damage = 0;
    private Long bornAt = 0L;
    private Vector3 force = new Vector3(0, 0, 0);
    private Vector3 drag = new Vector3(0, 0, 0);
    private Vector3 downwardAccel = new Vector3(0, 0, -.05f);
    private transient HiveNetConnection firedBy = null;
    private float speed = 0;
    protected boolean isFlying = true;

    public FlyingProjectile(HiveNetConnection firedBy, float speed) {
        this.firedBy = firedBy;
        this.speed = speed;
        this.force = firedBy.getForwardVector();
    }

    public FlyingProjectile() {
        this.isFlying = false;
    }

    @Override
    public void onSpawn() {
        this.bornAt = System.currentTimeMillis();
    }

    @Override
    public void onDespawn() {

    }

    public CombatHitResult checkCollision(Ray ray) {
        // Check entities in the chunk
        Vector3 hit = new Vector3();

        for (GameEntity entity : DedicatedServer.instance.getWorld().findCollisionEntites(this.location, 2500)) {
            if (CollisionEntity.class.isAssignableFrom(entity.getClass())) {

                CollisionEntity collisionEntity = (CollisionEntity) entity;

                // Is a collision entity
                if (Intersector.intersectRayBoundsFast(ray, collisionEntity.collisionBox())) {
                    // has hit a block or something else

                    for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                        connection.drawDebugBox(collisionEntity.collisionBox(), 2);
                    }

                    DedicatedServer.instance.getWorld().despawn(entity.uuid);

                    return new CombatEntityHitResult(this.firedBy, entity, hit);
                }
            }
        }

        // Check players
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            if (Intersector.intersectRayBoundsFast(ray, connection.getBoundingBox())) {
                if (this.location.toVector().dst(hit) <= 500) {
                    return new CombatPlayerHitResult(this.firedBy, connection, hit);
                } else {
                    System.out.println("Not in range");
                }
            }
        }

        return null;
    }

    @Override
    public void onTick() {
        if (this.aliveFor() > TimeUnit.SECONDS.toMillis(60)) {
            DedicatedServer.instance.getWorld().despawn(this.uuid);
            return;
        }

        if (this.location.getZ() <= -500) {
            this.isFlying = false;
            DedicatedServer.instance.getWorld().despawn(this.uuid);
            return;
        }

        Location f = this.location.cpy();
        Vector3 loc = this.location.toVector();

        if (this.isFlying) {
//        Vector3 fwdLoc = loc.cpy().mulAdd(this.firedBy.getForwardVector(),10);

            this.force.add(this.drag).add(this.downwardAccel);

            Vector3 fwdLoc = loc.cpy().mulAdd(this.force, (100 * this.speed));
            fwdLoc.add(this.force);

            this.location = Location.fromVector(fwdLoc);

            f.lookAt(this.location.cpy());
            this.location.setRotation(f.getRotation());

//            float deg = (float) VectorUtil.getDegrees(loc, this.location.toVector());
//            this.location.setRotation(0, 0, -deg);

            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//            connection.drawDebugLine(Location.fromVector(this.start), Location.fromVector(this.end), 2);
                connection.showArrowTrail(Location.fromVector(loc), this.location);
            }

            Vector3 dir = this.location.toVector().cpy().sub(loc);
            dir.nor();

            Ray r = new Ray(loc,dir);

            CombatHitResult hitResult = this.checkCollision(r);
            if (hitResult != null) {
                // Hit something
                hitResult.onHit(this.damage);
                this.isFlying = false;
                DedicatedServer.instance.getWorld().despawn(this.uuid);
                return;
            }
        }
    }

    public Long aliveFor() {
        return System.currentTimeMillis() - this.bornAt;
    }
}
