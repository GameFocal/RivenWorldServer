package com.gamefocal.rivenworld.events.player;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.util.Location;

public class PlayerMoveEvent extends Event<PlayerMoveEvent> {

    private HiveNetConnection connection;

    private Location location;

    public PlayerMoveEvent(HiveNetConnection connection, Location location) {
        this.connection = connection;
        this.location = location;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public Location getLocation() {
        return location;
    }
}
