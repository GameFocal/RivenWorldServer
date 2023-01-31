package com.gamefocal.island.game.enviroment.player;

import java.util.LinkedList;

public class PlayerEnvironmentEffect {

    public float hungerConsumptionPerTick;

    public float waterConsumptionPerTick;

    public float energyConsumptionPerTick;

    public float healthConsumptionPerTick;

    public LinkedList<PlayerDataState> addStates = new LinkedList<>();

    public LinkedList<PlayerDataState> removeStates = new LinkedList<>();

}
