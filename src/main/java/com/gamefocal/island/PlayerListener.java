package com.gamefocal.island;

import com.gamefocal.island.entites.events.EventHandler;
import com.gamefocal.island.entites.events.EventInterface;
import com.gamefocal.island.entites.events.EventPriority;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.events.entity.EntityDespawnEvent;
import com.gamefocal.island.events.entity.EntitySpawnEvent;
import com.gamefocal.island.events.player.PlayerMoveEvent;
import com.gamefocal.island.events.player.PlayerSpawnEvent;
import com.gamefocal.island.events.player.PlayerVoiceEvent;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.NetworkService;
import com.gamefocal.island.service.PlayerService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerListener implements EventInterface {

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
                    if (e.getValue() <= (5*50)) {
                        recv.add(DedicatedServer.get(PlayerService.class).players.get(e.getKey()));
                    }
                }

                break;

            case PROXIMITY_NORMAL:

                for (Map.Entry<UUID, Float> e : event.getSpeaker().getPlayerDistances().entrySet()) {
                    if (e.getValue() <= (10*50)) {
                        recv.add(DedicatedServer.get(PlayerService.class).players.get(e.getKey()));
                    }
                }

                break;

            case PROXIMITY_YELL:

                for (Map.Entry<UUID, Float> e : event.getSpeaker().getPlayerDistances().entrySet()) {
                    if (e.getValue() <= (25*50)) {
                        recv.add(DedicatedServer.get(PlayerService.class).players.get(e.getKey()));
                    }
                }

                break;
        }

        event.setRecivers(recv);
    }
}
