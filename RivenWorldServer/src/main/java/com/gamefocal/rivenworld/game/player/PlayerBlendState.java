package com.gamefocal.rivenworld.game.player;

import com.gamefocal.rivenworld.game.util.Location;

public class PlayerBlendState {

    public boolean isInAir = false;

    public float speed = 0.0f;

    public boolean isAiming = false;

    public boolean isFishing = false;

    public boolean isSwimming = false;

    public int attackMode = 0x00;

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

    public float BlendWUpper = 0.0f;

    public float BlendWRightArm = 0.0f;

}
