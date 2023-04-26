package com.gamefocal.rivenworld.game.ai;

import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

public abstract class AiGoal {

    protected LivingEntity attachedAt = null;
    protected boolean isComplete = false;

    public void attach(LivingEntity livingEntity) {
        this.attachedAt = livingEntity;
        this.onStart(livingEntity);
    }

    public void complete(LivingEntity livingEntity) {
        this.isComplete = true;
        this.onComplete(livingEntity);
    }

    public abstract void onStart(LivingEntity livingEntity);

    public abstract void onComplete(LivingEntity livingEntity);

    public abstract void onTick(LivingEntity livingEntity);
}
