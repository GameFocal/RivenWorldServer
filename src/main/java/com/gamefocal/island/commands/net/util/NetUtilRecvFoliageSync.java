package com.gamefocal.island.commands.net.util;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.service.FoliageService;

@Command(name = "fsync", sources = "udp")
public class NetUtilRecvFoliageSync extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        DedicatedServer.get(FoliageService.class).register(message.args[0], Location.fromString(message.args[1]));

    }
}
