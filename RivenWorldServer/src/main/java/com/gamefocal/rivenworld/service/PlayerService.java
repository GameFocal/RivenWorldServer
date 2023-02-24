package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@AutoService(HiveService.class)
public class PlayerService implements HiveService<PlayerService> {

    public ConcurrentHashMap<UUID, HiveNetConnection> players = new ConcurrentHashMap<>();

    public ArrayList<HiveNetConnection> findClosestPlayers(Location location) {

        ArrayList<HiveNetConnection> closest = new ArrayList<>(this.players.values());
        closest.sort(new Comparator<HiveNetConnection>() {
            @Override
            public int compare(HiveNetConnection o1, HiveNetConnection o2) {

                float dst1 = location.toVector().dst(o1.getPlayer().location.toVector());
                float dst2 = location.toVector().dst(o2.getPlayer().location.toVector());

                if (dst1 < dst2) {
                    return +1;
                } else if (dst1 > dst2) {
                    return -1;
                }

                return 0;
            }
        });

        return closest;
    }

    @Override
    public void init() {
    }
}
