package com.gamefocal.island.commands.net;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.models.Player;

@Command(name = "plmv", sources = "udp")
public class PlMvCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        Player p = netConnection.getPlayer();
        if (p != null) {
//            System.out.println("[PLMV:" + p.id + "]:: " + message.args[0]);

        }
    }
}
