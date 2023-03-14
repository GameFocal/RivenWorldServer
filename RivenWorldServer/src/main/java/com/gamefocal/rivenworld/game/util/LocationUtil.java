package com.gamefocal.rivenworld.game.util;

import com.badlogic.gdx.math.Vector3;

import java.util.List;
import java.util.Random;

public class LocationUtil {

    public static Location getClosestLocation(Location location, List<Location> locations) {

        locations.sort((o1, o2) -> {

            float dst1 = location.dist(o1);
            float dst2 = location.dist(o2);

            if (dst1 < dst2) {
                return -1;
            } else if (dst1 > dst2) {
                return +1;
            }

            return 0;
        });

        return locations.get(0);
    }

    public static Location getRandomLocationInRadius(int radius, Location currentLocation) {
        double ang = Math.random() * 2 * Math.PI,
                hyp = Math.sqrt(Math.random()) * radius,
                adj = Math.cos(ang) * hyp,
                opp = Math.sin(ang) * hyp;
        return new Location(currentLocation.getX() + adj, currentLocation.getY() + opp, 0);
    }

    public static Location projectLocationFromStartWithSpeed(Location location, Location goal, long timeInMilli, float speed) {
        // Speed is dist in seconds
        Vector3 vLoc = location.toVector();
        Vector3 vGoal = goal.toVector();

        float dst = vLoc.dst(vGoal);
        float timeInSeconds = timeInMilli / 1000f;
        float timeToTravel = dst / speed;
        Vector3 fwd = vGoal.cpy().sub(vLoc);


        float percentTraveled = timeInSeconds / timeToTravel;
        if (percentTraveled > 1) {
            percentTraveled = 1f;
        }

        float rad = VectorUtil.getDegrees(vLoc, vGoal);

        Vector3 projected = vLoc.lerp(vGoal, percentTraveled);
        Location p = Location.fromVector(projected).setRotation(0, 0, (long) rad-90);

        return p;
    }

}
