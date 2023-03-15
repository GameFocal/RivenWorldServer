package com.gamefocal.rivenworld.game.ai.goals;

import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FeedGoal extends AiGoal<Location> {

    private Long start;
    private Long complete;

    public FeedGoal() {
    }

    @Override
    public void onStart(LivingEntity livingEntity) {
        livingEntity.isFeeding = true;
        this.start = System.currentTimeMillis();
        this.complete = this.start + TimeUnit.SECONDS.toMillis(RandomUtil.getRandomNumberBetween(15, 35));
    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        if (System.currentTimeMillis() >= this.complete) {
            long diff = this.complete - this.start;
            livingEntity.energy -= ((float) diff / 1000 / 60);
            this.complete(livingEntity);
        }
    }

    @Override
    public void onEnd(LivingEntity livingEntity) {
        livingEntity.isFeeding = false;
    }

    @Override
    public void getState(Map<String, Object> meta) {

    }

    @Override
    public void onNetSync(Location location) {

    }
}
