package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.entites.net.*;

@Command(name = "a-e", sources = "tcp")
public class NetPlayerAction extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        /*
        * Process the Primary Action Event
        * */
        // TODO: Process the interact/ input here :)


    }
}
