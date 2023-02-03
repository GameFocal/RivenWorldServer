package com.gamefocal.island.game.entites.living;

import com.gamefocal.island.game.ai.AiStateMachine;
import com.gamefocal.island.game.ai.machines.PassiveAiStateMachine;
import com.gamefocal.island.game.entites.generics.LivingEntity;

public class Deer extends LivingEntity<Deer> {
    public Deer() {
        super(100, new PassiveAiStateMachine());
        this.type = "deer";
    }
}
