package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "FLY", sources = "tcp")
public class FlyCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            if (netConnection.isFlying()) {
                netConnection.sendTcp("FLY|f");
                netConnection.setFlying(false);
            } else {
                netConnection.hide();
                netConnection.sendTcp("FLY|t");
                netConnection.setFlying(true);
            }
        } else {
            System.out.println("Not a admin");
        }
    }
}
