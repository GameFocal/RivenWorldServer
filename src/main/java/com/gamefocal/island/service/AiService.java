package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.ai.AiState;
import com.gamefocal.island.game.entites.generics.LivingEntity;
import com.gamefocal.island.game.entites.living.Deer;
import com.gamefocal.island.game.util.TickUtil;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Singleton
@AutoService(HiveService.class)
public class AiService implements HiveService<AiService> {

    public Hashtable<Class<? extends LivingEntity>, Integer> population = new Hashtable<>();
    public Hashtable<Class<? extends LivingEntity>, Integer> currentSpawnCount = new Hashtable<>();

    public ConcurrentHashMap<UUID, LivingEntity> trackedEntites = new ConcurrentHashMap<>();

    public Long lastSpawnCheck = 0L;

    @Override
    public void init() {
        // Population Restrictions
        population.put(Deer.class, 25);

        // Spawn Animals
        TaskService.scheduleRepeatingTask(() -> {
            DedicatedServer.get(AiService.class).spawnNewAnimals();
        }, 20L, TickUtil.SECONDS(30), false);

        // AI Tick
        TaskService.scheduleRepeatingTask(() -> {
            DedicatedServer.get(AiService.class).tick();
        }, 20L, 20L, false);
    }

    public void spawnNewAnimals() {
        Long now = System.currentTimeMillis();
        int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(now - lastSpawnCheck);
        if (seconds > 15) {
            // Spawn Check

            for (Map.Entry<Class<? extends LivingEntity>, Integer> m : this.population.entrySet()) {

                int currentSpawned = 0;
                if (this.currentSpawnCount.containsKey(m.getKey())) {
                    currentSpawned = this.currentSpawnCount.get(m.getKey());
                }

                if (currentSpawned < m.getValue()) {
                    // Needs to spawn more of this...

                    for (int i = currentSpawned; i < m.getValue(); i++) {
                        
                    }

                }

            }

        }
    }

    public void tick() {
        for (LivingEntity entity : this.trackedEntites.values()) {
            if (entity.getStateMachine() != null) {
                // Has a state machine
                AiState state = entity.getStateMachine().onTick(entity);
                if (state != null) {
                    entity.setState(state);
                }

                entity.onTick();
            }
        }
    }

}
