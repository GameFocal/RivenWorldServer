package com.gamefocal.rivenworld.game.entites.projectile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatHitResult;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.entity.ProjectileMoveEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.DisposableEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.CombatService;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.concurrent.TimeUnit;

public abstract class FlyingProjectile<T> extends GameEntity<T> implements TickEntity, DisposableEntity {

    protected float damage = 0;
    protected boolean isFlying = true;
    protected boolean despawnOnHit = true;
    private Long bornAt = 0L;
    private Vector3 force = new Vector3(0, 0, 0);
    private Vector3 drag = new Vector3(0, 0, 0);
    private Vector3 downwardAccel = new Vector3(0, 0, -.01f);
    private transient HiveNetConnection firedBy = null;
    private float speed = 0;
    private float hitSize = 25;

    public FlyingProjectile(HiveNetConnection firedBy, float speed) {
        this.firedBy = firedBy;
        this.speed = speed;

        Vector3 playerRot = firedBy.getCameraLocation().toVector();
        Vector3 crossHair = firedBy.getCrossHairLocation().toVector();
//        crossHair.rotate(15,0,0,1);

        this.force = crossHair.sub(playerRot).nor();

//        firedBy.drawDebugLine(Color.RED, Location.fromVector(playerRot), Location.fromVector(playerRot.cpy().mulAdd(this.force, 500)), 2);
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

    public CombatHitResult checkCollision(Location location, HiveNetConnection connection, Ray ray) {
        // Check entities in the chunk

        CombatService.CombatRayHits hits = new CombatService.CombatRayHits(location, this.firedBy);
        hits.get(ray, 25000);

        return hits.applyDamage(damage);
    }

    @Override
    public void onTick() {
//        if (this.aliveFor() > TimeUnit.SECONDS.toMillis(60)) {
//            DedicatedServer.instance.getWorld().despawn(this.uuid);
//            return;
//        }

        try {
            if (this.location.getZ() <= DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(this.location)) {
                this.isFlying = false;
                DedicatedServer.instance.getWorld().despawn(this.uuid);
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.ARROW_HIT, this.location, 1500, 1, 1);
                return;
            }
        } catch (IllegalArgumentException e) {
            this.isFlying = false;
            DedicatedServer.instance.getWorld().despawn(this.uuid);
            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.ARROW_HIT, this.location, 1500, 1, 1);
            return;
        }

        Location f = this.location.cpy();
        Vector3 loc = this.location.toVector();

        if (this.isFlying) {
//        Vector3 fwdLoc = loc.cpy().mulAdd(this.firedBy.getForwardVector(),10);

            this.force.add(this.drag).add(this.downwardAccel);

            Vector3 fwdLoc = loc.cpy().mulAdd(this.force, (100 * this.speed));
//            fwdLoc.add(this.force);

            this.location = Location.fromVector(fwdLoc);

            f.lookAt(this.location.cpy());
            this.location.setRotation(f.getRotation());

            new ProjectileMoveEvent(this).call();

//            float deg = (float) VectorUtil.getDegrees(loc, this.location.toVector());
//            this.location.setRotation(0, 0, -deg);

            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//            connection.drawDebugLine(Location.fromVector(this.start), Location.fromVector(this.end), 2);
                connection.showArrowTrail(Location.fromVector(loc), this.location);
//                connection.drawDebugLine(Color.YELLOW,Location.fromVector(loc),this.location,1);
            }

            Vector3 dir = this.location.toVector().cpy().sub(loc);
            dir.nor();

            Ray r = new Ray(this.location.toVector(), this.force);

            CombatHitResult hitResult = this.checkCollision(Location.fromVector(loc), firedBy, r);
            if (hitResult != null) {
                // Hit something
//                hitResult.onHit(this.damage);
                this.isFlying = false;

                // Hit Sound
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.ARROW_HIT, this.location, 1500, 1, 1);

                if (this.despawnOnHit) {
                    DedicatedServer.instance.getWorld().despawn(this.uuid);
                }

                return;
            }
        }
    }

    public Long aliveFor() {
        return System.currentTimeMillis() - this.bornAt;
    }
}
