package com.gamefocal.island.events.building;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.util.Location;

public class BlockAttemptPlaceEvent extends Event<BlockAttemptPlaceEvent> {

    private HiveNetConnection connection;

    private Location location;

    private GameEntity<?> block;

    public BlockAttemptPlaceEvent(HiveNetConnection connection, Location location, GameEntity<?> block) {
        this.location = location;
        this.block = block;
        this.connection = connection;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public GameEntity<?> getBlock() {
        return block;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }
}
