package com.gamefocal.rivenworld.entites.prefab;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.util.Location;

import java.io.Serializable;

public class PrefabRecord implements Serializable {

    private GameEntity gameEntity;
    private Location relative;

    public PrefabRecord(GameEntity gameEntity, Location relative) {
        this.gameEntity = gameEntity;
        this.relative = relative;
    }

    public GameEntity getGameEntity() {
        return gameEntity;
    }

    public void setGameEntity(GameEntity gameEntity) {
        this.gameEntity = gameEntity;
    }

    public Location getRelative() {
        return relative;
    }

    public void setRelative(Location relative) {
        this.relative = relative;
    }
}
