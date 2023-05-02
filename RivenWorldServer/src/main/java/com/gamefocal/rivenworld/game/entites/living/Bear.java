package com.gamefocal.rivenworld.game.entites.living;

import com.gamefocal.rivenworld.game.ai.machines.PassiveAggroAiStateMachine;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

public class Bear extends LivingEntity<Bear> {
    public Bear() {
        super(400, new PassiveAggroAiStateMachine(300, 500, 60 * 3));
        this.type = "bear";
        this.speed = 1;
    }
}
