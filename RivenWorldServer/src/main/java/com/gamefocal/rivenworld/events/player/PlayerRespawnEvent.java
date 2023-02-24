package com.gamefocal.rivenworld.events.player;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.util.Location;

public class PlayerRespawnEvent extends Event<PlayerRespawnEvent> {

    private HiveNetConnection connection;

    private Location respawnLocation;

    public PlayerRespawnEvent(HiveNetConnection connection, Location respawnLocation) {
        this.connection = connection;
        this.respawnLocation = respawnLocation;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public Location getRespawnLocation() {
        return respawnLocation;
    }

    public void setRespawnLocation(Location respawnLocation) {
        this.respawnLocation = respawnLocation;
    }
}
