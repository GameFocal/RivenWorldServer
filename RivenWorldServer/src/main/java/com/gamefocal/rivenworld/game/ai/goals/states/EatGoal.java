package com.gamefocal.rivenworld.game.ai.goals.states;

import com.gamefocal.rivenworld.game.ai.goals.generic.StateGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.RandomUtil;

public class EatGoal extends StateGoal {
    @Override
    public long stateDuration() {
        return RandomUtil.getRandomNumberBetween(2,25);
    }

    @Override
    public void onStateEnter(LivingEntity livingEntity) {
        livingEntity.isFeeding = true;
    }

    @Override
    public void onStateExit(LivingEntity livingEntity) {
        livingEntity.isFeeding = false;
    }
}
