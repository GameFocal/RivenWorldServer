package com.gamefocal.rivenworld.commands.net;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.player.PlayerSpawnEvent;
import com.gamefocal.rivenworld.game.util.Location;

@Command(name = "loadworld", sources = "tcp")
public class NetLoadWorld extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // Spawn the world
        PlayerSpawnEvent event = new PlayerSpawnEvent(netConnection).call();

        if (event.isCanceled()) {
            return;
        }

        System.out.println("Player Spawned... Loading world.");
        event.getConnection().getPlayer().location = new Location(0, 0, 0);
        DedicatedServer.instance.getWorld().loadWorldForPlayer(event.getConnection());
    }
}