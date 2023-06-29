package com.gamefocal.rivenworld.listeners;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.game.ServerReadyEvent;
import com.gamefocal.rivenworld.events.world.SundownEvent;
import com.gamefocal.rivenworld.events.world.SunriseEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.living.Undead;
import com.gamefocal.rivenworld.game.entites.vfx.ShrineFogVFX;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.VectorUtil;
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

        for (Undead undead : DedicatedServer.instance.getWorld().getEntitesOfType(Undead.class)) {
            DedicatedServer.instance.getWorld().despawn(undead.uuid);
            // TODO: A Effect and sound here
        }

    }

    @EventHandler
    public void onSundownEvent(SundownEvent event) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            connection.syncToAmbientWorldSound();
        }

        // Spawn fog
        for (Location loc : DedicatedServer.get(ShrineService.class).shrineLocations) {
            DedicatedServer.instance.getWorld().spawn(new ShrineFogVFX(), DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(loc.cpy()).addZ(0));
        }

        int undeadAmt = DedicatedServer.get(ShrineService.class).shrineLocations.size();
        float daysUntilEvent = (DedicatedServer.get(EnvironmentService.class).dayNumber % 7);

        float spawnMultiple = MathUtils.map(1, 7, .5f, 5, daysUntilEvent);

        int spawnAmt = (undeadAmt * Math.round(spawnMultiple));

        System.out.println("Day " + DedicatedServer.get(EnvironmentService.class).dayNumber);
        System.out.println("Spawning " + spawnAmt + " on shrines...");

        for (int i = 0; i < spawnAmt; i++) {

            Location[] arr = DedicatedServer.get(ShrineService.class).shrineLocations.toArray(new Location[0]);

            Location shrineCenter = RandomUtil.getRandomElementFromArray(arr);

            float randomRot = RandomUtil.getRandomNumberBetween(0, 360);
            Vector2 start = new Vector2(shrineCenter.getX(), shrineCenter.getY());
            Vector2 end = VectorUtil.calculateV2Orbit(randomRot, RandomUtil.getRandomNumberBetween(100, 200), start.cpy());

            Location spawnPoint = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(new Location(end.x, end.y, 0));

            Undead undead = new Undead();
            undead.guardLocation = spawnPoint;
            undead.guardRadius = 2500;
            DedicatedServer.instance.getWorld().spawn(undead, spawnPoint);
        }
    }

}
