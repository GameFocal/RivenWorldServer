package com.gamefocal.rivenworld.events.entity;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.models.GameEntityModel;

public class EntityDespawnEvent extends Event<EntitySpawnEvent> {

    private GameEntityModel model;

    public EntityDespawnEvent(GameEntityModel model) {
        this.model = model;
    }

    public GameEntityModel getModel() {
        return model;
    }
}
