package com.gamefocal.island.events;

import com.gamefocal.island.entites.events.Event;
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
