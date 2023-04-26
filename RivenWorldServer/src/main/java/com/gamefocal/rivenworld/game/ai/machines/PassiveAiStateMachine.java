package com.gamefocal.rivenworld.game.ai.machines;

import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.ai.goals.MoveToLocationGoal;
import com.gamefocal.rivenworld.game.ai.goals.WanderGoal;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

public class PassiveAiStateMachine extends AiStateMachine {
    @Override
    public void onTick(LivingEntity livingEntity) {
        if (this.currentGoal == null && this.queue.size() == 0) {
            // Passive move around or eat
            System.out.println("Assigned Wander");
            this.assignGoal(livingEntity, new WanderGoal(livingEntity.location.cpy()));
        }
    }
}
