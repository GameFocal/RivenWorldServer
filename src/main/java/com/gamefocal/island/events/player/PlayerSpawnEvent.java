package com.gamefocal.island.events.player;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.util.Location;

public class PlayerSpawnEvent extends Event<PlayerSpawnEvent> {

    private HiveNetConnection connection;

    public PlayerSpawnEvent(HiveNetConnection connection) {
        this.connection = connection;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }
}
