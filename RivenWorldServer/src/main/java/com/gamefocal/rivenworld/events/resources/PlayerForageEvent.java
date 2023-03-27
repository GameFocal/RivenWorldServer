package com.gamefocal.rivenworld.events.resources;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ray.HitResult;

public class PlayerForageEvent extends Event<PlayerForageEvent> {

    private HiveNetConnection connection;

    private HitResult hitResult;

    public PlayerForageEvent(HiveNetConnection connection, HitResult hitResult) {
        this.connection = connection;
        this.hitResult = hitResult;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public HitResult getHitResult() {
        return hitResult;
    }
}
