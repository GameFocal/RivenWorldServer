package com.gamefocal.island.events.building;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.game.util.Location;

public class BlockPlaceEvent extends Event<BlockPlaceEvent> {

    private Location location;

    private String placedOnActor;

    public BlockPlaceEvent(Location location, String placedOnActor) {
        this.location = location;
        this.placedOnActor = placedOnActor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPlacedOnActor() {
        return placedOnActor;
    }

    public void setPlacedOnActor(String placedOnActor) {
        this.placedOnActor = placedOnActor;
    }
}
