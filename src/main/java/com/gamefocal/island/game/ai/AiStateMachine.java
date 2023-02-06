package com.gamefocal.island.game.ai;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.entites.generics.LivingEntity;

public abstract class AiStateMachine {

    public abstract AiState onAttacked(HiveNetConnection by, LivingEntity attacked);

    public abstract AiState onTick(LivingEntity entity);

}
