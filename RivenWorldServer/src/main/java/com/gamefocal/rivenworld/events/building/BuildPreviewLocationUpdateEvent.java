package com.gamefocal.rivenworld.events.building;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.util.Location;

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
