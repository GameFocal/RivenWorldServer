package com.gamefocal.rivenworld.events.building;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.util.Location;

public class PropAttemptPlaceEvent extends Event<PropAttemptPlaceEvent> {

    private Location location;

    private HiveNetConnection connection;

    private GameEntity prop;

    public PropAttemptPlaceEvent(Location location, HiveNetConnection connection, GameEntity prop) {
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
