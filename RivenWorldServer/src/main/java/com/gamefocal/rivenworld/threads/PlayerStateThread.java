package com.gamefocal.rivenworld.threads;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.events.player.PlayerStateSyncEvent;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@AsyncThread(name = "player-state")
public class PlayerStateThread implements HiveAsyncThread {

    private ConcurrentHashMap<UUID, Long> playerSpawnDelay = new ConcurrentHashMap<>();

    @Override
    public void run() {
        while (true) {
            try {

                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {

                    if (connection.getDragging() != null) {
                        Vector3 dragPoint = connection.getPlayer().location.toVector();
                        dragPoint.mulAdd(connection.getForwardVector(), 100);
                        dragPoint.z += 50;
                        connection.getDragging().tpToLocation(Location.fromVector(dragPoint));
//                        connection.getDragging().getPlayer().location = Location.fromVector(dragPoint);
                        DedicatedServer.get(PlayerService.class).players.get(connection.getDragging().getUuid()).getPlayer().location = Location.fromVector(dragPoint);
                    }

                    for (HiveNetConnection peer : DedicatedServer.get(PlayerService.class).players.values()) {
                        if (!peer.getPlayer().id.equalsIgnoreCase(connection.getPlayer().id)) {

//                            if (!peer.isLoaded()) {
//                                continue;
//                            }

                            boolean isNearby = false;
                            boolean isLoaded = false;
                            boolean isDead = peer.getState().isDead;
                            boolean isHidden = !peer.isVisible();

                            isLoaded = connection.getLoadedPlayers().containsKey(peer.getUuid());
                            isNearby = (connection.getLOD(peer.getPlayer().location) <= 1);

                            if (isLoaded && (isHidden || isDead)) {
                                connection.sendHidePacket(peer);
                                connection.getLoadedPlayers().remove(peer.getUuid());
                                playerSpawnDelay.remove(peer.getUuid());
                                continue;
                            }

                            if (isHidden) {
                                continue;
                            }

                            if (isLoaded && !isNearby) {
                                connection.sendHidePacket(peer);
                                connection.getLoadedPlayers().remove(peer.getUuid());
                                playerSpawnDelay.remove(peer.getUuid());
                            } else if (!isLoaded && isNearby) {
                                connection.getLoadedPlayers().put(peer.getUuid(), "fresh");
                            }

                            if (connection.getLoadedPlayers().containsKey(peer.getUuid())) {

//                                boolean firstLoad = false;
//                                if (connection.getLoadedPlayers().get(peer.getUuid()).equalsIgnoreCase("fresh")) {
//                                    firstLoad = true;
//                                }

                                if (!connection.getLoadedPlayers().get(peer.getUuid()).equalsIgnoreCase(peer.playStateHash())) {
                                    peer.getState().tick();

                                    connection.sendStatePacket(peer);
                                    connection.getLoadedPlayers().put(peer.getUuid(), peer.playStateHash());
                                }
                            }
                        }
                    }

                    new PlayerStateSyncEvent(connection).call();
                }

                Thread.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.yield();
            }
        }
    }
}
