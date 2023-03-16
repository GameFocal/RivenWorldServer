package com.gamefocal.rivenworld.game.ai.goals;

import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.Map;

public abstract class LocationGoal extends AiGoal<LinkedList<Location>> {
    public LocationGoal(Location location) {
        this.location = location;
    }

    @Override
    public void onStart(LivingEntity livingEntity) {

    }

    @Override
    public void onTick(LivingEntity livingEntity) {

    }

    @Override
    public void onEnd(LivingEntity livingEntity) {

    }

    @Override
    public void getState(Map<String, Object> meta) {

    }

    @Override
    public void onNetSync(LinkedList<Location> locations) {

    }
}
