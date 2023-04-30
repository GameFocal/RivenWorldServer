package com.gamefocal.rivenworld.game.ai.goals.generic;

import com.gamefocal.rivenworld.game.ai.AiGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

import java.util.concurrent.TimeUnit;

public abstract class StateGoal extends AiGoal {

    protected long startAt;

    protected long exitAt;

    public abstract long stateDuration();

    public abstract void onStateEnter(LivingEntity livingEntity);

    public abstract void onStateExit(LivingEntity livingEntity);

    @Override
    public void onStart(LivingEntity livingEntity) {
        this.onStateEnter(livingEntity);
        this.startAt = System.currentTimeMillis();
        this.exitAt = this.startAt + TimeUnit.SECONDS.toMillis(this.stateDuration());
    }

    @Override
    public void onComplete(LivingEntity livingEntity) {
        this.onStateExit(livingEntity);
    }

    @Override
    public void onTick(LivingEntity livingEntity) {
        if(System.currentTimeMillis() > this.exitAt) {
            this.complete(livingEntity);
        }
    }
}
