package com.gamefocal.rivenworld.game.ai;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.AiService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AiSpawn implements Serializable {

    private Class<? extends LivingEntity> type;
    private LinkedList<Location> locations = new LinkedList<>();
    private long totalAlive = 0;
    private long lastSpawn = 0L;
    private long delayInSeconds = 60;

    public AiSpawn(Class<? extends LivingEntity> type, long lastSpawn, long total, long delayInSeconds, Location... locations) {
        this.type = type;
        this.lastSpawn = lastSpawn;
        this.totalAlive = total;
        this.delayInSeconds = delayInSeconds;
        this.locations.addAll(Arrays.asList(locations));
    }

    public int getCurrentSpawned() {
        int current = 0;
        for (UUID uuid : DedicatedServer.get(AiService.class).trackedEntites) {
            GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(uuid);
            if (e != null) {
                if (type.isAssignableFrom(e.entityData.getClass())) {
                    current++;
                }
            }
        }
        return current;
    }

    public int spawn() {

        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastSpawn) < this.delayInSeconds) {
            return 0;
        }

        int current = getCurrentSpawned();
        int spawned = 0;

        if (current < totalAlive) {
            float spawnAmt = totalAlive - current;
            for (int i = 0; i < spawnAmt; i++) {
                Location randLoc = RandomUtil.getRandomElementFromList(this.locations);
                if (randLoc != null) {
                    try {
                        DedicatedServer.instance.getWorld().spawn(type.newInstance(), randLoc);
                        spawned++;
                        this.lastSpawn = System.currentTimeMillis();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (spawned > 0) {
            this.lastSpawn = System.currentTimeMillis();
        }

        return spawned;
    }

    public Class<? extends LivingEntity> getType() {
        return type;
    }

    public void setType(Class<? extends LivingEntity> type) {
        this.type = type;
    }

    public LinkedList<Location> getLocations() {
        return locations;
    }

    public void setLocations(LinkedList<Location> locations) {
        this.locations = locations;
    }

    public long getLastSpawn() {
        return lastSpawn;
    }

    public void setLastSpawn(long lastSpawn) {
        this.lastSpawn = lastSpawn;
    }

    public long getDelayInSeconds() {
        return delayInSeconds;
    }

    public void setDelayInSeconds(long delayInSeconds) {
        this.delayInSeconds = delayInSeconds;
    }
}
