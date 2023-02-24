package com.gamefocal.rivenworld.entites.net;

import java.util.LinkedList;

public abstract class HiveCommand {

    private LinkedList<CommandSource> allowedSources = new LinkedList<>();

    private String name;

    public abstract void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception;

    public void runCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) {

        boolean canRun = (this.allowedSources.size() == 0 || this.allowedSources.contains(source));

        if((source == CommandSource.NET_TCP || source == CommandSource.NET_UDP) && netConnection.getNetworkMode() == NetworkMode.TCP_ONLY) {
//            source = CommandSource.NET_UDP;
            canRun = true;
        }

        if (canRun) {
            try {
                this.onCommand(message, source, netConnection);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
