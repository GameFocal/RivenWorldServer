package com.gamefocal.rivenworld.events.game;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public class ServerWorldSyncEvent extends Event<ServerTickEvent> {

    private HiveNetConnection connection;

    public ServerWorldSyncEvent(HiveNetConnection connection) {
        this.connection = connection;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }
}
