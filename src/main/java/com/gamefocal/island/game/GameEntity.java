package com.gamefocal.island.game;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.service.NetworkService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;

public abstract class GameEntity<T> implements Serializable {

    public String unrealClass = "Actor";

}
