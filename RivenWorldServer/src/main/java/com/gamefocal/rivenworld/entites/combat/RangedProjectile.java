package com.gamefocal.rivenworld.entites.combat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
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

    private float zOffset;

    private float aliveFor;

    private HiveNetConnection source;

    public RangedProjectile(HiveNetConnection source, float angleInDegrees, float velocity, Location fireLocation, Vector3 fwdVector, long rangeInCms) {
        this.uuid = UUID.randomUUID();
        this.angleInDegrees = angleInDegrees;
        this.velocity = velocity;
        this.fireLocation = fireLocation;
        this.fwdVector = fwdVector;
        this.maxRange = rangeInCms;
        this.source = source;
    }

    public HiveNetConnection getSource() {
        return source;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void fire() {
        this.firedAt = System.currentTimeMillis();

//        if (this.fwdVector.y < 0) {
//            this.angleInDegrees *= -1;
//        }

//        start = this.fireLocation.toVector();
//        aliveFor = this.maxRange / this.velocity;
//        System.out.println("fowd vector: " + this.fwdVector);
//        this.fwdVector = this.fwdVector.rotate(new Vector3(0,0,1), 2.5F);
//        this.zOffset = (float) Math.tan(Math.toRadians(this.angleInDegrees));
//        this.zOffset *= this.maxRange;
//        this.end = this.start.cpy().add(this.fwdVector.mulAdd(this.fwdVector, this.maxRange));
//        this.end = this.end.add(new Vector3(0,0, this.zOffset - 100));



//        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
////            connection.drawDebugLine(Location.fromVector(this.start), Location.fromVector(this.end), 2);
//            connection.showArrowTrail(Location.fromVector(this.start), Location.fromVector(this.end));
//        }

        this.dieAt = (long) (this.firedAt + Math.round(aliveFor));
    }

    private double getProjectedZ(long timeInSeconds) {
        return ((this.velocity * timeInSeconds) * Math.sin(this.angleInDegrees)) - ((0.00000980665 / 2) * Math.pow(timeInSeconds, 2));
    }

    public boolean isDead() {
        return (System.currentTimeMillis() > this.dieAt);
    }

    public Ray getProjectedWorldSpace(long timeInSeconds) {
        float alpha = (timeInSeconds / this.aliveFor);

        Quaternion quaternion = new Quaternion();
        quaternion.setFromAxis(new Vector3(0, 0, 1), this.angleInDegrees);

        Vector3 lerp = start.cpy().lerp(this.end, alpha);

        Vector3 dir = lerp.cpy().sub(start);
        dir.nor();

        return new Ray(lerp, dir);

//        return Location.fromVector(lerp);
    }

    public Ray getProjectedSpace() {
        int i = (int) ((int) System.currentTimeMillis() - this.firedAt);
        return this.getProjectedWorldSpace(i);
    }

}
