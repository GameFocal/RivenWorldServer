package com.gamefocal.rivenworld.game.entites.living;

import com.gamefocal.rivenworld.game.ai.machines.PassiveAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

public class Doe extends LivingEntity<Doe> {
    public Doe() {
        super(100, new PassiveAiStateMachine());
        this.type = "Doe";
        this.speed = 2;
    }
}
