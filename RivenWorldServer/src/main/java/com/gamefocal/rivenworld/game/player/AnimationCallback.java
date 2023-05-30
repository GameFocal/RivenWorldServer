package com.gamefocal.rivenworld.game.player;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public interface AnimationCallback {
    public void onRun(HiveNetConnection connection, String[] args);
}
