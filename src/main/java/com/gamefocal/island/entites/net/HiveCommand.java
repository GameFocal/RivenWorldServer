package com.gamefocal.island.entites.net;

import com.gamefocal.island.entites.orm.models.Player;

public abstract class HiveCommand {

    private CommandSource[] allowedSources = new CommandSource[0];

    private String name;

    public abstract void onCommand(HiveNetMessage message, CommandSource source, Player player);

}
