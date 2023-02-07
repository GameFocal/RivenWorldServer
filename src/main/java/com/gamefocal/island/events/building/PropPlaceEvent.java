package com.gamefocal.island.events.building;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.util.Location;

public class PropPlaceEvent extends Event<PropPlaceEvent> {

    private Location location;

    private HiveNetConnection connection;

    private GameEntity prop;

    public PropPlaceEvent(Location location, HiveNetConnection connection, GameEntity prop) {
        this.location = location;
        this.connection = connection;
        this.prop = prop;
    }

    public Location getLocation() {
        return location;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public GameEntity getProp() {
        return prop;
    }
}
