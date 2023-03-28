package com.gamefocal.rivenworld.game.entites.living;

import com.gamefocal.rivenworld.game.ai.machines.PassiveAiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

public class Deer extends LivingEntity<Deer> {
    public Deer() {
        super(100, new PassiveAiStateMachine());
        this.type = "deer";
        this.speed = 125;
    }
}
