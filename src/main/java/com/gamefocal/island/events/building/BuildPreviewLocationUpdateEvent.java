package com.gamefocal.island.events.building;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.util.Location;

public class BuildPreviewLocationUpdateEvent extends Event<BuildPreviewLocationUpdateEvent> {

    private HiveNetConnection connection;

    private Location location;

    private boolean canBuild = false;

    public BuildPreviewLocationUpdateEvent(HiveNetConnection connection, Location location) {
        this.connection = connection;
        this.location = location;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isCanBuild() {
        return canBuild;
    }

    public void setCanBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }
}
