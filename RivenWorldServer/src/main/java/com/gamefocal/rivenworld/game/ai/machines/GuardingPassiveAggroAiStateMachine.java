package com.gamefocal.rivenworld.game.ai.machines;

import com.gamefocal.rivenworld.game.ai.goals.move.GuardGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

import java.util.HashMap;

public class GuardingPassiveAggroAiStateMachine extends PassiveAggroAiStateMachine {

    public GuardingPassiveAggroAiStateMachine(float aggroTriggerDistance, float aggroLossDistance, long aggroTimeLimitInSeconds) {
        super(aggroTriggerDistance, aggroLossDistance, aggroTimeLimitInSeconds);
    }

    @Override
    public void onInit(LivingEntity livingEntity) {
//        super.onInit(livingEntity);
        this.goalTable.put(null, new HashMap<>() {{
            put(new GuardGoal(livingEntity.guardLocation, livingEntity.guardRadius), 1);
        }});
    }
}
