package com.gamefocal.island.entites.net;

import com.gamefocal.island.entites.orm.models.Player;

import java.util.LinkedList;

public abstract class HiveCommand {

    private LinkedList<CommandSource> allowedSources = new LinkedList<>();

    private String name;

    public abstract void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection);

    public void runCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) {
        if (this.allowedSources.size() == 0 || this.allowedSources.contains(source)) {
            this.onCommand(message, source, netConnection);
        }
    }

    public LinkedList<CommandSource> getAllowedSources() {
        return allowedSources;
    }

    public void setAllowedSources(LinkedList<CommandSource> allowedSources) {
        this.allowedSources = allowedSources;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
