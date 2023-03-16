package com.gamefocal.rivenworld.game.ai.goals;

import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RestGoal extends AiGoal<Location> {
    private Long start;
    private Long complete;

    public RestGoal() {
    }

    @Override
    public void onStart(LivingEntity livingEntity) {
        livingEntity.isResting = true;
        this.start = System.currentTimeMillis();
        this.complete = this.start + TimeUnit.MINUTES.toMillis(RandomUtil.getRandomNumberBetween(2, 6));
    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        if (System.currentTimeMillis() >= this.complete) {
            this.complete(livingEntity);
        }
    }

    @Override
    public void onEnd(LivingEntity livingEntity) {
        livingEntity.isResting = false;
        livingEntity.energy = 100f;
    }

    @Override
    public void getState(Map<String, Object> meta) {

    }

    @Override
    public void onNetSync(Location location) {

    }
}
