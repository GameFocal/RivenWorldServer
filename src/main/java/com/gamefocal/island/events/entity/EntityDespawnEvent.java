package com.gamefocal.island.events.entity;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.events.entity.EntitySpawnEvent;
import com.gamefocal.island.models.GameEntityModel;

public class EntityDespawnEvent extends Event<EntitySpawnEvent> {

    private GameEntityModel model;

    public EntityDespawnEvent(GameEntityModel model) {
        this.model = model;
    }

    public GameEntityModel getModel() {
        return model;
    }
}
