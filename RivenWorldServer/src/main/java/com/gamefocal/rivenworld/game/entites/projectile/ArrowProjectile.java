package com.gamefocal.rivenworld.game.entites.projectile;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ArrowProjectile extends GameEntity<ArrowProjectile> implements TickEntity {

    private Long bornAt = 0L;
    private float maxDistance = 1500;
    private Vector3 force = new Vector3(0, 0, 0);
    private Vector3 drag = new Vector3(0, 0, 0);
    private Vector3 downwardAccel = new Vector3(0, 0, -.05f);
    private UUID firedBy = null;

    public ArrowProjectile(float speed, HiveNetConnection firedBy) {

        this.type = "NetArrow";
//        this.force = VectorUtil.fromYawPitch(firedBy.getRotVector().y*360,firedBy.getRotVector().z*360).mul(
//                new Quaternion(2.5f, 2.5f, 2.5f, 0)
//        );

        this.force = firedBy.getForwardVector();

        // Up/down
        System.out.println(firedBy.getRotVector());

        this.force.mul(new Quaternion(speed, speed, speed, 0));

        this.firedBy = firedBy.getUuid();

        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            connection.drawDebugLine(firedBy.getPlayer().location,
                    Location.fromVector(firedBy.getPlayer().location.toVector().mulAdd(firedBy.getPlayer().location.toVector(), 300)),
                    5);
        }
    }

    public ArrowProjectile() {
        this.type = "NetArrow";
    }

    @Override
    public void onSpawn() {
        this.bornAt = System.currentTimeMillis();
    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {
        if (this.aliveFor() > TimeUnit.SECONDS.toMillis(15)) {
            DedicatedServer.instance.getWorld().despawn(this.uuid);
            return;
        }

        /*
         * Adjust the location and check for collisions
         * */
        this.force.add(this.drag).add(downwardAccel);

        Vector3 loc = this.location.toVector();

        loc.add(this.force);

        this.location = Location.fromVector(loc);

        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            connection.drawDebugSphere(this.location, 5, 2);
        }
    }

    public Long aliveFor() {
        return System.currentTimeMillis() - this.bornAt;
    }
}
