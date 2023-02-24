package com.gamefocal.rivenworld.events.building;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.util.Location;

public class BlockPlaceEvent extends Event<BlockPlaceEvent> {

    private HiveNetConnection connection;

    private Location location;

    private GameEntity<?> block;

    public BlockPlaceEvent(HiveNetConnection connection, Location location, GameEntity<?> block) {
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
