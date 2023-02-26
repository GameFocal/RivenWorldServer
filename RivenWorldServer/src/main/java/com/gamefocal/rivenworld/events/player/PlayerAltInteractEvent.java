package com.gamefocal.rivenworld.events.player;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public class PlayerAltInteractEvent extends Event<PlayerAltInteractEvent> {
    private HiveNetConnection connection;

    public PlayerAltInteractEvent(HiveNetConnection connection) {
        this.connection = connection;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }
}
