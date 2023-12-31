package com.gamefocal.rivenworld.events.entity;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameEntityModel;

public class EntitySpawnEvent extends Event<EntitySpawnEvent> {

    private GameEntity entity;

    private Location location;

    private GameEntityModel model;

    public EntitySpawnEvent(GameEntity entity, Location location) {
        this.entity = entity;
        this.location = location;
    }

    public EntitySpawnEvent(GameEntityModel model) {
        this.model = model;
        this.entity = model.entityData;
        this.location = model.location;
    }

    public GameEntity getEntity() {
        return entity;
    }

    public Location getLocation() {
        return location;
    }

    public GameEntityModel getModel() {
        return model;
    }
}
