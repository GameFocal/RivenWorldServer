package com.gamefocal.island.events.building;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.util.Location;

public class BlockAttemptDestroyEvent extends Event<BlockAttemptDestroyEvent> {

    private HiveNetConnection connection;

    private Location location;

    private GameEntity blockEntity;

    public BlockAttemptDestroyEvent(HiveNetConnection connection, Location location, GameEntity blockEntity) {
        this.connection = connection;
        this.location = location;
        this.blockEntity = blockEntity;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public Location getLocation() {
        return location;
    }

    public GameEntity getBlockEntity() {
        return blockEntity;
    }
}
