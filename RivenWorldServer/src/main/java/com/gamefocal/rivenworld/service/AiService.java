package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.entites.vote.PeerVoteRequest;
import com.gamefocal.rivenworld.game.ai.AiPathRequest;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.entites.living.Deer;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator;
import com.github.czyzby.noise4j.map.generator.util.Generators;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Singleton
@AutoService(HiveService.class)
public class AiService implements HiveService<AiService> {

    public Hashtable<Class<? extends LivingEntity>, Integer> population = new Hashtable<>();
    public Hashtable<Class<? extends LivingEntity>, Integer> currentSpawnCount = new Hashtable<>();

    public ConcurrentHashMap<UUID, LivingEntity> trackedEntites = new ConcurrentHashMap<>();

    public LinkedList<BoundingBox> noEnterZones = new LinkedList<>();

    private Grid animalSpawnLocations;

    private static void noiseStage(final Grid grid, final NoiseGenerator noiseGenerator, final int radius,
                                   final float modifier) {
        noiseGenerator.setRadius(radius);
        noiseGenerator.setModifier(modifier);
        // Seed ensures randomness, can be saved if you feel the need to
        // generate the same map in the future.
        noiseGenerator.setSeed(Generators.rollSeed());
        noiseGenerator.generate(grid);
    }

    @Override
    public void init() {
        // Population Restrictions
        population.put(Deer.class, 25);

        // Spawn Animals
//        TaskService.scheduleRepeatingTask(() -> {
//            DedicatedServer.get(AiService.class).spawnNewAnimals();
//        }, 20L, TickUtil.SECONDS(30), false);

        // AI Tick
//        TaskService.scheduleRepeatingTask(() -> {
//            DedicatedServer.get(AiService.class).tick();
//        }, 20L, 20L, false);

        animalSpawnLocations = new Grid(1008);

//        CellularAutomataGenerator cellularGenerator = new CellularAutomataGenerator();
//        cellularGenerator.setAliveChance(0.5f);
//        cellularGenerator.setRadius(2);
//        cellularGenerator.setBirthLimit(13);
//        cellularGenerator.setDeathLimit(9);
//        cellularGenerator.setIterationsAmount(6);
//        cellularGenerator.generate(grid);

        NoiseGenerator noiseGenerator = new NoiseGenerator();
        noiseStage(animalSpawnLocations, noiseGenerator, 32, 0.6f);
        noiseStage(animalSpawnLocations, noiseGenerator, 16, 0.2f);
        noiseStage(animalSpawnLocations, noiseGenerator, 8, 0.1f);
        noiseStage(animalSpawnLocations, noiseGenerator, 4, 0.1f);
        noiseStage(animalSpawnLocations, noiseGenerator, 1, 0.05f);

        for (Class<? extends LivingEntity> l : this.population.keySet()) {
            this.currentSpawnCount.put(l, 0);
        }
    }

    public void processAiTick() {
        for (LivingEntity livingEntity : this.trackedEntites.values()) {
            livingEntity.onTick();
        }
    }

    public BoundingBox addAiNoEnterZone(Location a, Location b) {
        BoundingBox boundingBox = LocationUtil.getBox(a, b);
        this.noEnterZones.add(boundingBox);
        return boundingBox;
    }

    public void spawnNewAnimals() {
        if (DedicatedServer.instance.getWorld() != null) {
            for (Map.Entry<Class<? extends LivingEntity>, Integer> m : this.population.entrySet()) {

                int currentSpawned = 0;
                if (this.currentSpawnCount.containsKey(m.getKey())) {
                    currentSpawned = this.currentSpawnCount.get(m.getKey());
                }

                if (currentSpawned < m.getValue()) {
                    // Needs to spawn more of this...

//                    System.out.println("Spawning " + (m.getValue() - currentSpawned) + " of " + m.getKey().getSimpleName());

                    for (int i = currentSpawned; i < m.getValue(); i++) {
                        Location randomLoc = DedicatedServer.instance.getWorld().randomLocationInGrid(animalSpawnLocations, .45f, 4000);
                        if (randomLoc != null) {

                            DedicatedServer.get(RayService.class).makeRequest(randomLoc, 3, request -> {
                                // Spawn the animal
                                try {
                                    LivingEntity le = m.getKey().newInstance();

                                    DedicatedServer.instance.getWorld().spawn(le, request.getReturnedLocation());

                                    this.trackedEntites.put(le.uuid, le);
                                    this.currentSpawnCount.put(m.getKey(), this.currentSpawnCount.get(m.getKey()) + 1);

                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                }
            }
        }
    }
}
