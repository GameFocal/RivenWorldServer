package com.gamefocal.rivenworld.events.player;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public class PlayerInteractEvent extends Event<PlayerInteractEvent> {
    private HiveNetConnection connection;

    public PlayerInteractEvent(HiveNetConnection connection) {
        this.connection = connection;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }
}
