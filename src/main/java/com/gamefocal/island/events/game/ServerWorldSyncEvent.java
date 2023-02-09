package com.gamefocal.island.events.game;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;

public class ServerWorldSyncEvent extends Event<ServerTickEvent> {

    private HiveNetConnection connection;

    public ServerWorldSyncEvent(HiveNetConnection connection) {
        this.connection = connection;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }
}
