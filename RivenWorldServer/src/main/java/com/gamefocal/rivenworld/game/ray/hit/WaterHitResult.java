package com.gamefocal.rivenworld.game.ray.hit;

import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.water.WaterSource;

public class WaterHitResult implements HitResult<WaterSource> {

    private Location location;

    private WaterSource source;

    public WaterHitResult(Location location, WaterSource source) {
        this.location = location;
        this.source = source;
    }

    public Location getLocation() {
        return location;
    }

    public WaterSource getSource() {
        return source;
    }

    @Override
    public WaterSource get() {
        return this.source;
    }
}
