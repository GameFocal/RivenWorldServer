package com.gamefocal.island.events;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.PlayerModel;

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
