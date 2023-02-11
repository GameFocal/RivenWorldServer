package com.gamefocal.island.game.ray.hit;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.ray.HitResult;

public class EntityHitResult implements HitResult<GameEntity> {

    private GameEntity entity;

    public EntityHitResult(GameEntity entity) {
        this.entity = entity;
    }

    @Override
    public GameEntity get() {
        return this.entity;
    }
}
