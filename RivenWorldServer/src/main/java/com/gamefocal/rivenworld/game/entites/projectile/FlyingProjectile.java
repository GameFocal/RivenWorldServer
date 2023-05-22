package com.gamefocal.rivenworld.game.entites.projectile;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatHitResult;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.DisposableEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
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
    private Vector3 downwardAccel = new Vector3(0, 0, -.05f);
    private transient HiveNetConnection firedBy = null;
    private float speed = 0;

    public FlyingProjectile(HiveNetConnection firedBy, float speed) {
        this.firedBy = firedBy;
        this.speed = speed;

        Vector3 playerRot = firedBy.getPlayer().location.toVector();
        Vector3 crossHair = firedBy.getCrossHairLocation().toVector();
//        crossHair.rotate(15,0,0,1);

        this.force = crossHair.sub(playerRot.add(0,0,100)).nor();
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
        hits.get(ray, 5000);

        return hits.applyDamage(damage);
    }

    @Override
    public void onTick() {
//        if (this.aliveFor() > TimeUnit.SECONDS.toMillis(60)) {
//            DedicatedServer.instance.getWorld().despawn(this.uuid);
//            return;
//        }

        if(this.location.getZ() <= DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(this.location)) {
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

            Ray r = new Ray(loc, dir);

            CombatHitResult hitResult = this.checkCollision(Location.fromVector(loc), firedBy, r);
            if (hitResult != null) {
                // Hit something
//                hitResult.onHit(this.damage);
                this.isFlying = false;

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
