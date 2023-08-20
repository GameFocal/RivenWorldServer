package com.gamefocal.rivenworld.commands.chat;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.entites.placable.decoration.BedPlaceable;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.gamefocal.rivenworld.service.RespawnService;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Command(name = "stuck", sources = "chat")
public class StuckCommand extends HiveCommand {

    public static HashMap<UUID,Long> stuckTimeOut = new HashMap<>();

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        if (netConnection.inCombat()) {
            netConnection.sendChatMessage(ChatColor.RED + "You can not use this while in combat.");
            return;
        }

        if(stuckTimeOut.containsKey(netConnection.getUuid())) {
            if(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - stuckTimeOut.get(netConnection.getUuid())) <= 30) {
                netConnection.sendChatMessage(ChatColor.RED + " You can only use unstuck every 30 minutes.");
                return;
            }
        }

//        netConnection.sendChatMessage(ChatColor.ORANGE + "Please stand still while we respawn you...");

        final Location initLoc = netConnection.getPlayer().location;

        TaskService.schedulePlayerInterruptTask(() -> {
            float d = netConnection.getPlayer().location.dist(initLoc);
            Location closest = DedicatedServer.get(RespawnService.class).randomSpawnLocation();
            BedPlaceable respawnBed = netConnection.getRespawnBed();
            if (respawnBed != null && respawnBed.location.dist(initLoc) > (50*100)) {
                if (respawnBed.location.dist(closest) > 500) {
                    closest = respawnBed.location.cpy().addZ(300);
                }
            }

            netConnection.tpToLocation(closest);

            stuckTimeOut.put(netConnection.getUuid(),System.currentTimeMillis());
        },15L,"Respawning", Color.GREEN,netConnection);

/*
        TaskService.scheduledDelayTask(() -> {
            float d = netConnection.getPlayer().location.dist(initLoc);
            if (d <= 500) {

                Location closest = DedicatedServer.get(RespawnService.class).randomSpawnLocation();

                BedPlaceable respawnBed = netConnection.getRespawnBed();
                if (respawnBed != null) {
                    if (respawnBed.location.dist(closest) > 500) {
                        closest = respawnBed.location.cpy().addZ(300);
                    }
                }

                netConnection.tpToLocation(closest);
            } else {
                netConnection.sendChatMessage(ChatColor.RED + "Canceled stuck command... please do not move while attempting to TP.");
            }
        }, TickUtil.SECONDS(5), false);
*/
    }
}
