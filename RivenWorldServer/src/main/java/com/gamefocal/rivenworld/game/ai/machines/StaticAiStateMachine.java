package com.gamefocal.rivenworld.game.ai.machines;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.AiState;
import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

public class StaticAiStateMachine extends AiStateMachine {
    @Override
    public AiState onAttacked(HiveNetConnection by, LivingEntity attacked) {
        return null;
    }

    @Override
    public AiState onSpooked(HiveNetConnection by, LivingEntity livingEntity, float influence) {
        return null;
    }

    @Override
    public AiState onTick(LivingEntity entity) {
        return null;
    }
}
