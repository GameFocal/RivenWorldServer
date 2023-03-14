package com.gamefocal.rivenworld.game.ai.goals;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.gson.JsonObject;

import java.util.Map;

public class FindEntityGoal extends AiGoal<GameEntity> {
    public FindEntityGoal(Class<? extends GameEntity> t) {
        // TODO: Find closest of this entity and attempt to go there
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
    public void onNetSync(GameEntity gameEntity) {

    }
}
