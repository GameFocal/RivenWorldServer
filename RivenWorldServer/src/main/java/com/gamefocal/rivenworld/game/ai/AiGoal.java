package com.gamefocal.rivenworld.game.ai;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.gson.JsonObject;

import java.util.Map;

public abstract class AiGoal<T> {

    protected Location location;
    protected HiveNetConnection connection;
    protected GameEntity entity;
    protected boolean isComplete = false;

    public abstract void onStart(LivingEntity livingEntity);

    public abstract void onTick(LivingEntity livingEntity);

    public abstract void onEnd(LivingEntity livingEntity);

    public abstract void getState(Map<String, Object> meta);

    public abstract void onNetSync(T t);

    public boolean isComplete() {
        return isComplete;
    }

    public void complete(LivingEntity entity) {
        this.onEnd(entity);
        this.isComplete = true;
    }

    public T t() {
        return (T) getClass().getTypeParameters()[0];
    }

}
