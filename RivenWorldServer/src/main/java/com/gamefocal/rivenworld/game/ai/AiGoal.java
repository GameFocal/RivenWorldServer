package com.gamefocal.rivenworld.game.ai;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.gson.JsonObject;

import java.util.Map;

public abstract class AiGoal {

    protected Location location;
    protected HiveNetConnection connection;
    protected GameEntity entity;
    protected boolean isComplete = false;

    public abstract void onStart(LivingEntity livingEntity);

    public abstract void onTick(LivingEntity livingEntity);

    public abstract void onEnd(LivingEntity livingEntity);

    public abstract void takeOwnership(LivingEntity livingEntity, HiveNetConnection connection);

    public abstract void releaseOwnership(LivingEntity livingEntity);

    public abstract void onOwnershipCmd(LivingEntity livingEntity, HiveNetConnection connection, String cmd, JsonObject data);

    public abstract boolean validatePeerUpdate(LivingEntity livingEntity, HiveNetConnection connection, Location location);

    public boolean isComplete() {
        return isComplete;
    }

    public void restart(LivingEntity livingEntity) {
        this.onEnd(livingEntity);
        this.onStart(livingEntity);
    }

    public void complete(LivingEntity entity) {
        this.onEnd(entity);
        this.isComplete = true;
    }
}
