package com.gamefocal.rivenworld.events.interact;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.util.Location;

public class PlayerInteractEvent extends Event<PlayerInteractEvent> {

    private HiveNetConnection connection;

    private InteractAction action;

    private Location location;

    private GameEntity entity;

    public PlayerInteractEvent(HiveNetConnection connection, InteractAction action, Location location, GameEntity entity) {
        this.connection = connection;
        this.action = action;
        this.location = location;
        this.entity = entity;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public InteractAction getAction() {
        return action;
    }

    public Location getLocation() {
        return location;
    }

    public GameEntity getEntity() {
        return entity;
    }
}
