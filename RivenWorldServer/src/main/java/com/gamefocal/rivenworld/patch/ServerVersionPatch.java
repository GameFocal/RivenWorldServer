package com.gamefocal.rivenworld.patch;

public interface ServerVersionPatch {

    public float version();

    public void onPatch();

    public boolean shouldPatch();

}
