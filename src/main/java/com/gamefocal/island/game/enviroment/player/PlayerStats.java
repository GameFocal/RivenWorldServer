package com.gamefocal.island.game.enviroment.player;

import java.util.LinkedList;

public class PlayerStats {

    public float health = 100f;

    public float magic = 100f;

    public float energy = 100f;

    public float hunger = 100f;

    public float thirst = 100f;

    public LinkedList<PlayerDataState> states = new LinkedList<>();

}
