package com.gamefocal.rivenworld.game.util;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.goals.agro.TargetPlayerGoal;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.LinkedList;

public class PlayerUtil {

    public static LinkedList<HiveNetConnection> getPlayersInRange(Location location, float dist) {
        LinkedList<HiveNetConnection> inRange = new LinkedList<>();
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            if (connection.getPlayer().location.dist(location) <= dist) {
                inRange.add(connection);
            }
        }

        if (inRange.size() > 0) {
            inRange.sort((o1, o2) -> {
                float dst1 = o1.getPlayer().location.dist(location);
                float dst2 = o2.getPlayer().location.dist(location);

                if (dst2 < dst1) {
                    return +1;
                } else if (dst2 > dst1) {
                    return -1;
                }

                return 0;
            });
        }

        return inRange;
    }

    public static LinkedList<HiveNetConnection> getClosestPlayers(Location location) {
        LinkedList<HiveNetConnection> inRange = new LinkedList<>(DedicatedServer.get(PlayerService.class).players.values());

        if (inRange.size() > 0) {
            inRange.sort((o1, o2) -> {
                float dst1 = o1.getPlayer().location.dist(location);
                float dst2 = o2.getPlayer().location.dist(location);

                if (dst2 < dst1) {
                    return +1;
                } else if (dst2 > dst1) {
                    return -1;
                }

                return 0;
            });
        }

        return inRange;
    }

    public static HiveNetConnection getClosestPlayer(Location location, float dist) {
        return getPlayersInRange(location, dist).get(0);
    }

}
