package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.entites.net.*;

@Command(name = "invmv", sources = "tcp")
public class MoveItemCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // invmv|{inv}|{slot}|{amt}|{toslot}

    }
}
