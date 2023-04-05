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

    public boolean IsCrouching = false;

    public boolean hasTorch = false;

    public boolean IsMoving = false;

    public boolean hasBow = false;

    public float DeltaTimeX = 0.0f;

    public Location Velocity = new Location(0, 0, 0);

    public float GroundSpeed = 0.0f;

    public Location BaseAimRotation = new Location(0, 0, 0);

    public float YawOffset = 0.0f;

    public boolean hasShield = false;

    public boolean twoHand = false;

    public boolean oneHand = false;

    public boolean hasSpear = false;

    public boolean isCapture = false;

}
