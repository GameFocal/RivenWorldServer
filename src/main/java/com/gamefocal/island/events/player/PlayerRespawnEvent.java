package com.gamefocal.island.events.player;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.util.Location;

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
