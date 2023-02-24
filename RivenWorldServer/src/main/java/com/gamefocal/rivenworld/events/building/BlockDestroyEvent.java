package com.gamefocal.rivenworld.events.building;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.util.Location;

public class BlockDestroyEvent extends Event<BlockDestroyEvent> {

    private HiveNetConnection connection;

    private Location location;

    private GameEntity blockEntity;

    public BlockDestroyEvent(HiveNetConnection connection, Location location, GameEntity blockEntity) {
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
