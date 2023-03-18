package com.gamefocal.rivenworld.entites.combat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RangedProjectile {

    private UUID uuid;

    private float angleInDegrees;

    private float velocity;

    private Location fireLocation;

    private Vector3 fwdVector;

    private long firedAt;

    private long dieAt;

    private long maxRange;

    private Vector3 start;

    private Vector3 end;

    private float aliveFor;

    public RangedProjectile(float angleInDegrees, float velocity, Location fireLocation, Vector3 fwdVector, long rangeInCms) {
        this.uuid = UUID.randomUUID();
        this.angleInDegrees = angleInDegrees;
        this.velocity = velocity;
        this.fireLocation = fireLocation;
        this.fwdVector = fwdVector;
        this.maxRange = rangeInCms;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void fire() {
        this.firedAt = System.currentTimeMillis();

        if (this.fwdVector.y < 0) {
            this.angleInDegrees *= -1;
        }

        start = this.fireLocation.toVector();
        aliveFor = this.maxRange / this.velocity;
        this.end = start.cpy().mulAdd(this.fwdVector, this.maxRange).rotate(start.cpy().nor(), this.angleInDegrees);

        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            connection.showArrowTrail(Location.fromVector(this.start), Location.fromVector(this.end));
        }

        this.dieAt = (long) (this.firedAt + Math.round(aliveFor));
    }

    private double getProjectedZ(long timeInSeconds) {
        return ((this.velocity * timeInSeconds) * Math.sin(this.angleInDegrees)) - ((0.00000980665 / 2) * Math.pow(timeInSeconds, 2));
    }

    public boolean isDead() {
        return (System.currentTimeMillis() > this.dieAt);
    }

    public Location getProjectedWorldSpace(long timeInSeconds) {
        float alpha = (timeInSeconds / this.aliveFor);

        Quaternion quaternion = new Quaternion();
        quaternion.setFromAxis(new Vector3(0, 0, 1), this.angleInDegrees);

        Vector3 lerp = start.cpy().lerp(this.end, alpha);

        return Location.fromVector(lerp);
    }

    public Location getProjectedSpace() {
        int i = (int) ((int) System.currentTimeMillis() - this.firedAt);
        return this.getProjectedWorldSpace(i);
    }

}
