package com.gamefocal.rivenworld.events.player;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public class PlayerSpawnEvent extends Event<PlayerSpawnEvent> {

    private HiveNetConnection connection;

    public PlayerSpawnEvent(HiveNetConnection connection) {
        this.connection = connection;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }
}
