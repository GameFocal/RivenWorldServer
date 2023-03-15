package com.gamefocal.rivenworld.game.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ProjectedLocation {

    private Location source;
    private Location goal;
    private long timeInMilli;
    private float speedCmps;
    private float percent;
    private float deg;
    private Location fwdVector;
    private float dist;
    private float totalTravelTime;
    private Location position;

    public ProjectedLocation(Location source, Location goal, long timeInMilli, float speedCmps) {
        this.source = source;
        this.goal = goal;
        this.timeInMilli = timeInMilli;
        this.speedCmps = speedCmps;

        Vector3 vLoc = this.source.toVector();
        Vector3 vGoal = this.goal.toVector();

        this.dist = vLoc.dst(vGoal);
        float timeInSeconds = this.timeInMilli / 1000f;
        this.totalTravelTime = this.dist / this.speedCmps;
//        this.fwdVector = Location.fromVector(vGoal.cpy().sub(vLoc));

        this.percent = timeInSeconds / this.totalTravelTime;
        if (this.percent > 1) {
            this.percent = 1f;
        }

        float deg = (float) VectorUtil.getDegrees(vLoc, vGoal);

        System.out.println(deg);

        Vector3 projected = vLoc.lerp(vGoal, this.percent);

        this.position = Location.fromVector(projected).setRotation(0, 0, deg);
    }

    public float getPercent() {
        return percent;
    }

    public float getDeg() {
        return deg;
    }

    public Location getFwdVector() {
        return fwdVector;
    }

    public float getDist() {
        return dist;
    }

    public float getTotalTravelTime() {
        return totalTravelTime;
    }

    public Location getPosition() {
        return position;
    }
}
