package com.gamefocal.island.game.ai;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.entites.generics.LivingEntity;

public interface AiStateMachine {

    public AiState onAttacked(HiveNetConnection by, LivingEntity attacked);

    public AiState onTick(LivingEntity entity);

}
