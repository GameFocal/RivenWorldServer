package com.gamefocal.island.events.entity;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;

public class LeftClickEntityEvent extends Event<LeftClickEntityEvent> {

    private HiveNetConnection netConnection;

    private GameEntityModel entityModel;

    private Location fromLocation;

    public LeftClickEntityEvent(HiveNetConnection netConnection, GameEntityModel entityModel, Location fromLocation) {
        this.netConnection = netConnection;
        this.entityModel = entityModel;
        this.fromLocation = fromLocation;
    }

    public HiveNetConnection getNetConnection() {
        return netConnection;
    }

    public void setNetConnection(HiveNetConnection netConnection) {
        this.netConnection = netConnection;
    }

    public GameEntityModel getEntityModel() {
        return entityModel;
    }

    public void setEntityModel(GameEntityModel entityModel) {
        this.entityModel = entityModel;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Location fromLocation) {
        this.fromLocation = fromLocation;
    }
}
