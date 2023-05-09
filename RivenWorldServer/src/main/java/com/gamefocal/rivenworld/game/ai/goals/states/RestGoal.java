package com.gamefocal.rivenworld.game.ai.goals.states;

import com.gamefocal.rivenworld.game.ai.goals.generic.StateGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.RandomUtil;

public class RestGoal extends StateGoal {
    @Override
    public long stateDuration() {
        return RandomUtil.getRandomNumberBetween(1,15);
    }

    @Override
    public void onStateEnter(LivingEntity livingEntity) {
        livingEntity.isResting = true;
    }

    @Override
    public void onStateExit(LivingEntity livingEntity) {
        livingEntity.isResting = false;
    }
}
