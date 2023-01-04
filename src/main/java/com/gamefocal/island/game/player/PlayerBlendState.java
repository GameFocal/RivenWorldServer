package com.gamefocal.island.game.player;

import com.gamefocal.island.game.util.Location;

public class PlayerBlendState {

    public boolean isInAir = false;

    public float speed = 0.0f;

    public Location aimRotator = new Location(0,0,0);

    public boolean isAiming = false;

    public float rotation = 0.0f;

    public boolean isFishing = false;

    public boolean isSwimming = false;

}
