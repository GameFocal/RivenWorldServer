package com.gamefocal.rivenworld.game.entites.living;

import com.gamefocal.rivenworld.game.ai.machines.PassiveAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

public class Rabbit extends LivingEntity<Rabbit> {
    public Rabbit() {
        super(100, new PassiveAiStateMachine());
        this.type = "Rabbit";
        this.speed = 2;
    }
}
