package com.gamefocal.rivenworld.game.ai;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class AiPathRequest {

    private LivingEntity livingEntity;
    private ConcurrentHashMap<UUID, LinkedList<Location>> requests = new ConcurrentHashMap<>();

    public AiPathRequest(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    public void addPeer(HiveNetConnection connection) {
        this.requests.put(connection.getUuid(), new LinkedList<>());
    }

    public void reply(HiveNetConnection connection, String[] payload) {
        if (this.requests.containsKey(connection.getUuid())) {
            for (int i = 1; i < payload.length; i++) {
                this.requests.get(connection.getUuid()).add(Location.fromString(payload[i]));
            }
        }
    }

    public boolean isComplete() {
        for (LinkedList<Location> l : this.requests.values()) {
            if (l.size() == 0) {
                return false;
            }
        }

        return true;
    }

    public int getPathCount() {
        int i = Integer.MAX_VALUE;
        for (LinkedList<Location> l : this.requests.values()) {
            if (l.size() < i && l.size() > 0) {
                i = l.size();
            }
        }

        return i;
    }

    public LinkedList<Location> getBestPath() {
        return (LinkedList<Location>) RandomUtil.getRandomElementFromArray(this.requests.values().toArray());
    }

}
