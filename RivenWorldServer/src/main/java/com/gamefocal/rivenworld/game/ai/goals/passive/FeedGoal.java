package com.gamefocal.rivenworld.game.ai.goals.passive;

import com.gamefocal.rivenworld.game.ai.goals.generic.PassiveGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

public class FeedGoal extends PassiveGoal {
    public FeedGoal() {
        super(1, 3);
    }

    @Override
    public void onStart(LivingEntity livingEntity) {
        livingEntity.isFeeding = true;
        super.onStart(livingEntity);
    }

    @Override
    public void onEnd(LivingEntity livingEntity) {
        livingEntity.isFeeding = false;
    }
}
