package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.events.EventPriority;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.player.PlayerSpawnEvent;
import com.gamefocal.rivenworld.events.player.PlayerVoiceEvent;
import com.gamefocal.rivenworld.events.world.SundownEvent;
import com.gamefocal.rivenworld.events.world.SunriseEvent;
import com.gamefocal.rivenworld.service.PlayerService;
import com.gamefocal.rivenworld.service.RespawnService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerListener implements EventInterface {

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnEvent event) {
//        if (event.getConnection().getPlayer().location == null) {
//            event.getConnection().tpToLocation(DedicatedServer.get(RespawnService.class).randomSpawnLocation());
//        } else {
//            event.getConnection().tpToLocation(event.getConnection().getPlayer().location);
//        }
    }

    @EventHandler
    public void onSunriseEvent(SunriseEvent event) {
//        System.out.println("SUNRISE EVENT");
//        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//            connection.syncToAmbientWorldSound();
//        }
    }

    @EventHandler
    public void onSunriseEvent(SundownEvent event) {
//        System.out.println("SUNSET EVENT");
//        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//            connection.syncToAmbientWorldSound();
//        }
    }

    @EventHandler(priority = EventPriority.FIRST)
    public void onPlayerVoiceEvent(PlayerVoiceEvent event) {

        List<HiveNetConnection> recv = new ArrayList<>();

        recv.add(event.getSpeaker());

        switch (event.getType()) {
            case GLOBAL:
                // Speak to everyone
                recv = new ArrayList<>(DedicatedServer.get(PlayerService.class).players.values());
                break;

            case PROXIMITY_WHISPER:

                // Speak to only
                for (Map.Entry<UUID, Float> e : event.getSpeaker().getPlayerDistances().entrySet()) {
                    if (e.getValue() <= (5 * 50)) {
                        recv.add(DedicatedServer.get(PlayerService.class).players.get(e.getKey()));
                    }
                }

                break;

            case PROXIMITY_NORMAL:

                for (Map.Entry<UUID, Float> e : event.getSpeaker().getPlayerDistances().entrySet()) {
                    if (e.getValue() <= (10 * 50)) {
                        recv.add(DedicatedServer.get(PlayerService.class).players.get(e.getKey()));
                    }
                }

                break;

            case PROXIMITY_YELL:

                for (Map.Entry<UUID, Float> e : event.getSpeaker().getPlayerDistances().entrySet()) {
                    if (e.getValue() <= (25 * 50)) {
                        recv.add(DedicatedServer.get(PlayerService.class).players.get(e.getKey()));
                    }
                }

                break;
        }

        event.setRecivers(recv);
    }
}
