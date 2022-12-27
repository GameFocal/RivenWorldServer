package com.gamefocal.island.commands.net.combat;

import com.gamefocal.island.entites.net.*;

@Command(name = "nhit",sources = "tcp")
public class NetHitEntity extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // Hit a entity
        System.out.println(message.toString());

    }
}
