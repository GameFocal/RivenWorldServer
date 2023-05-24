package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "FP", sources = "tcp")
public class FPCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isFirstPerson()) {
            netConnection.sendTcp("FP|f");
            netConnection.setFP(false);
            System.out.println("FP off");
        } else {
            netConnection.sendTcp("FP|t");
            netConnection.setFP(true);
            System.out.println("FP on");
        }
    }
}
