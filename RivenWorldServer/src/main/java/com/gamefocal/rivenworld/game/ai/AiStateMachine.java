package com.gamefocal.rivenworld.game.ai;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;

public abstract class AiStateMachine {

    public AiStateMachine() {
    }

    public abstract AiState onAttacked(HiveNetConnection by, LivingEntity attacked);

    public abstract AiState onTick(LivingEntity entity);

}
