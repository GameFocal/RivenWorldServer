package com.gamefocal.rivenworld.game.ray.hit;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ray.HitResult;

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
