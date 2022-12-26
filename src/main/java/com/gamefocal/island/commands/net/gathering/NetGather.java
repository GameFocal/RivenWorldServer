package com.gamefocal.island.commands.net.gathering;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.util.Location;

@Command(name = "ng",sources = "tcp")
public class NetGather extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // ng|type|loc|data

        String type = message.args[0];
        Location location = Location.fromString(message.args[1]);
        String misc = message.args[2];

        System.out.println(message.toString());

    }
}
