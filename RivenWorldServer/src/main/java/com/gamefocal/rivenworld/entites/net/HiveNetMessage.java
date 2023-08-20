package com.gamefocal.rivenworld.entites.net;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.service.CommandService;

import java.util.Arrays;

public class HiveNetMessage {

    public String cmd;

    public String[] args = new String[]{"n"};

    @Override
    public String toString() {
        return DedicatedServer.get(CommandService.class).msgToString(this);
    }

    public String argAsString() {
        return String.join("|", this.args);
    }

    public String getAsStringAtIndex(int i) {
        return String.join(" ", Arrays.copyOfRange(this.args, i, this.args.length));
    }

    public String argAsSingle() {
        return String.join(" ", this.args);
    }
}
