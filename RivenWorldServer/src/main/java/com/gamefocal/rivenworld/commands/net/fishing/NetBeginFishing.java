package com.gamefocal.rivenworld.commands.net.fishing;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "fishb", sources = "tcp")
public class NetBeginFishing extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        netConnection.getPlayer().setIsFishing(true);
    }
}
