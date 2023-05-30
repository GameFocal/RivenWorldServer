package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "speed", sources = "chat")
public class speedtestCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            netConnection.SetSpeed(Float.parseFloat(message.args[0]));
        }

//        if (message.argAsString().equalsIgnoreCase("sprint")) {
//            netConnection.SetSpeed("sprint", 5000);
//        } else if (message.argAsString().equalsIgnoreCase("swim")) {
//            netConnection.SetSpeed("swim", 1000);
//        } else if (message.argAsString().equalsIgnoreCase("fly")) {
//            netConnection.SetSpeed("fly", 3000);
//        } else if (message.argAsString().equalsIgnoreCase("crouch")) {
//            netConnection.SetSpeed("crouch", 600);
//        }
//        else if (message.argAsString().equalsIgnoreCase("reset")){
//            netConnection.SetSpeed("sprint", 1000);
//            netConnection.SetSpeed("swim", 300);
//            netConnection.SetSpeed("fly", 8000);
//            netConnection.SetSpeed("crouch", 300);
//        }
    }
}
