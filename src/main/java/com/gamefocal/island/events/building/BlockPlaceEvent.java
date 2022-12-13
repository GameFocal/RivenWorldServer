package com.gamefocal.island.events.building;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.util.Location;

public class BlockPlaceEvent extends Event<BlockPlaceEvent> {

    private Location location;

    private GameEntity<?> block;

    public BlockPlaceEvent(Location location, GameEntity<?> block) {
        this.location = location;
        this.block = block;
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
}
