package com.gamefocal.island.commands.net.fishing;

import com.gamefocal.island.entites.net.*;

@Command(name = "fishe", sources = "tcp")
public class NetStopFishing extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        netConnection.getPlayer().setIsFishing(false);
    }
}
