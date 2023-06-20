package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.vfx.ShrineFogVFX;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

@AutoService(HiveService.class)
@Singleton
public class ShrineService implements HiveService<ShrineService> {

    public ConcurrentLinkedQueue<Location> shrineLocations = new ConcurrentLinkedQueue<>();
    public ConcurrentLinkedQueue<GameEntity> shrineVFX = new ConcurrentLinkedQueue<>();

    @Override
    public void init() {
        shrineLocations.add(Location.fromString("127143.76,108474.04,7559.2183,0.0,0.0,-85.40732"));
        shrineLocations.add(Location.fromString("122445.586,37289.723,6397.342,0.0,0.0,164.06454"));
        shrineLocations.add(Location.fromString("1497.3593,81069.766,9762.352,0.0,0.0,117.75881"));
        shrineLocations.add(Location.fromString("37429.477,142791.56,12110.899,0.0,0.0,-121.77663"));
        shrineLocations.add(Location.fromString("69469.58,123769.945,24625.844,0.0,0.0,-65.81827"));

        for (ShrineFogVFX e : DedicatedServer.instance.getWorld().getEntitesOfType(ShrineFogVFX.class)) {
            DedicatedServer.instance.getWorld().despawn(e.uuid);
        }
    }
}
