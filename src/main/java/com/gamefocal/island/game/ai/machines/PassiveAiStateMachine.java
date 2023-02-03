package com.gamefocal.island.game.ai.machines;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.ai.AiState;
import com.gamefocal.island.game.ai.AiStateMachine;

import java.util.LinkedList;
import java.util.UUID;

public class PassiveAiStateMachine implements AiStateMachine {

    public LinkedList<UUID> avoidPlayers = new LinkedList<>();

    @Override
    public AiState onAttacked(HiveNetConnection by) {
        this.avoidPlayers.add(by.getUuid());
        return AiState.HIDE;
    }

    @Override
    public AiState onTick() {

        /*
        * TODO: Make a choice to run away and to what location
        * */

        return null;
    }
}
