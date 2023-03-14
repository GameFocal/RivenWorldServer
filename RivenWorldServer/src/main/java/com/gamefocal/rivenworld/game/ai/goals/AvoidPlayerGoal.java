package com.gamefocal.rivenworld.game.ai.goals;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.google.gson.JsonObject;

import java.util.Map;

public class AvoidPlayerGoal extends AiGoal<HiveNetConnection> {
    public AvoidPlayerGoal() {
        // TODO: Avoid any players
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
    public void onNetSync(HiveNetConnection connection) {

    }
}
