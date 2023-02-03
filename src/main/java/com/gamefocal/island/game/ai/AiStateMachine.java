package com.gamefocal.island.game.ai;

import com.gamefocal.island.entites.net.HiveNetConnection;

public interface AiStateMachine {

    public AiState onAttacked(HiveNetConnection by);

    public AiState onTick();

}
