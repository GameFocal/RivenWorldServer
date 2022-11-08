package com.gamefocal.island;

import com.gamefocal.island.entites.events.EventHandler;
import com.gamefocal.island.entites.events.EventInterface;
import com.gamefocal.island.entites.events.EventPriority;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.events.PlayerMoveEvent;
import com.gamefocal.island.events.PlayerSpawnEvent;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.service.NetworkService;

public class PlayerListener implements EventInterface {

    @EventHandler(priority = EventPriority.LAST)
    public void onPlayerMove(PlayerMoveEvent event) {
        // Send the player move to other clients
        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "plmv";
        message.args = new String[]{event.getConnection().getUuid().toString(), event.getLocation().toString()};
        DedicatedServer.get(NetworkService.class).broadcastUdp(message, event.getConnection().getUuid());
    }

    @EventHandler(priority = EventPriority.LAST)
    public void onPlayerSpawn(PlayerSpawnEvent event) {
        System.out.println("Player Spawned... Loading world.");
        event.getConnection().getPlayer().location = new Location(0, 0, 0);
        DedicatedServer.instance.getWorld().loadWorldForPlayer(event.getConnection());
    }

}
