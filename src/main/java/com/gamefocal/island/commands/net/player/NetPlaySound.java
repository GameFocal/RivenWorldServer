package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.sounds.GameSounds;
import com.gamefocal.island.service.TaskService;

@Command(name = "psfx", sources = "tcp")
public class NetPlaySound extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        String sound = message.args[0];

        try {
            GameSounds sounds = GameSounds.valueOf(sound);

            TaskService.scheduledDelayTask(()->{
                DedicatedServer.instance.getWorld().playSoundAtLocation(sounds, netConnection.getPlayer().location, 5, 1.0f, 1.0f);
            },5L,false);

        } catch (Exception e) {
            System.err.println("Invalid Sound ID");
        }
    }
}
