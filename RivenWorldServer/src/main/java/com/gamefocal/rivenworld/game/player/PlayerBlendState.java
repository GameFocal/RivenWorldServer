package com.gamefocal.rivenworld.game.player;

import com.gamefocal.rivenworld.game.util.Location;

public class PlayerBlendState {

    public boolean isInAir = false;

    public float speed = 0.0f;

    public Location aimRotator = new Location(0, 0, 0);

    public boolean isAiming = false;

    public float rotation = 0.0f;

    public boolean isFishing = false;

    public boolean isSwimming = false;

    public Location attackAngle = new Location(0, 0, 0);

    public int attackMode = 0x00;

    public int attackDirection = 0x00;

}
