package com.gamefocal.rivenworld.game.ai.goals.generic;

import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class PassiveGoal extends AiGoal {

    protected int min;
    protected Long start;
    protected Long complete;
    protected int max;

    public PassiveGoal(int minTime, int maxTime) {
        this.min = minTime;
        this.max = maxTime;
    }

    @Override
    public void onStart(LivingEntity livingEntity) {
        this.start = System.currentTimeMillis();
        this.complete = this.start + TimeUnit.SECONDS.toMillis(RandomUtil.getRandomNumberBetween(this.min, this.max));
    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        if (System.currentTimeMillis() >= this.complete) {
            this.complete(livingEntity);
        }
    }

    @Override
    public void getState(Map<String, Object> meta) {

    }
}
