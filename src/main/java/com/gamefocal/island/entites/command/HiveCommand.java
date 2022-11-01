package com.gamefocal.island.entites.command;

import com.gamefocal.island.entites.net.HiveCommandMessage;
import com.gamefocal.island.entites.net.HiveReplyMessage;
import com.gamefocal.island.entites.net.NetCommandSource;

public abstract class HiveCommand {

    public String name;

    public NetCommandSource allowedSource = NetCommandSource.ANY;

    public abstract HiveReplyMessage onCommand(HiveCommandMessage object);

}
