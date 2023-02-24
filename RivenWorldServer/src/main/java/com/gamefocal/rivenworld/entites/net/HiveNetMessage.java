package com.gamefocal.rivenworld.entites.net;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.service.CommandService;

public class HiveNetMessage {

    public String cmd;

    public String[] args = new String[]{"n"};

    @Override
    public String toString() {
        return DedicatedServer.get(CommandService.class).msgToString(this);
    }

    public String argAsString() {
        return String.join("|",this.args);
    }
}
