package com.gamefocal.island.commands.net.fishing;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.service.FishingService;

@Command(name = "fishb", sources = "tcp")
public class NetBeginFishing extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        netConnection.getPlayer().setIsFishing(true);
    }
}
