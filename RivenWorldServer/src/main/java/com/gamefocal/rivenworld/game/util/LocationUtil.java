package com.gamefocal.rivenworld.game.util;

import java.util.List;

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

}
