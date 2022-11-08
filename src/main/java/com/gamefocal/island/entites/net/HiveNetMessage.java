package com.gamefocal.island.entites.net;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.service.CommandService;

public class HiveNetMessage {

    public String cmd;

    public String[] args;

    @Override
    public String toString() {
        return DedicatedServer.get(CommandService.class).msgToString(this);
    }
}
