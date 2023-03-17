package com.gamefocal.rivenworld.game.ai.goals.passive;

import com.gamefocal.rivenworld.game.ai.goals.generic.PassiveGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.Map;

public class RestGoal extends PassiveGoal {
    public RestGoal() {
        super(3, 15);
    }

    @Override
    public void onStart(LivingEntity livingEntity) {
        livingEntity.isResting = true;
        super.onStart(livingEntity);
    }

    @Override
    public void onEnd(LivingEntity livingEntity) {
        livingEntity.isResting = false;
    }
}
