package com.gamefocal.rivenworld.game.ray.hit;

import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.util.Location;

public class TerrainHitResult implements HitResult<String> {

    private Location location;

    private String typeOfGround;

    public TerrainHitResult(Location location, String typeOfGround) {
        this.location = location;
        this.typeOfGround = typeOfGround;
    }

    public Location getLocation() {
        return location;
    }

    public String getTypeOfGround() {
        return typeOfGround;
    }

    @Override
    public String get() {
        return this.getTypeOfGround();
    }
}
