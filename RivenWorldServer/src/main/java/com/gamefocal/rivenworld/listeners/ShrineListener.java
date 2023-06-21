package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.game.ServerReadyEvent;
import com.gamefocal.rivenworld.events.world.SundownEvent;
import com.gamefocal.rivenworld.events.world.SunriseEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.vfx.ShrineFogVFX;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.EnvironmentService;
import com.gamefocal.rivenworld.service.PlayerService;
import com.gamefocal.rivenworld.service.ShrineService;

public class ShrineListener implements EventInterface {

    @EventHandler
    public void serverReadyEvent(ServerReadyEvent readyEvent) {
        for (ShrineFogVFX e : DedicatedServer.instance.getWorld().getEntitesOfType(ShrineFogVFX.class)) {
            DedicatedServer.instance.getWorld().despawn(e.uuid);
        }

        if (!DedicatedServer.get(EnvironmentService.class).isDay) {
            // Spawn fog
            for (Location loc : DedicatedServer.get(ShrineService.class).shrineLocations) {
                DedicatedServer.instance.getWorld().spawn(new ShrineFogVFX(), DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(loc.cpy()).addZ(0));
            }
        }
    }

    @EventHandler
    public void onSunriseEvent(SunriseEvent event) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            connection.syncToAmbientWorldSound();
        }

        // despawn the fog
        for (GameEntity e : DedicatedServer.get(ShrineService.class).shrineVFX) {
            DedicatedServer.instance.getWorld().despawn(e.uuid);
        }
    }

    @EventHandler
    public void onSunriseEvent(SundownEvent event) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            connection.syncToAmbientWorldSound();
        }

        // Spawn fog
        for (Location loc : DedicatedServer.get(ShrineService.class).shrineLocations) {
            DedicatedServer.instance.getWorld().spawn(new ShrineFogVFX(), DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(loc.cpy()).addZ(0));
        }
    }

}
