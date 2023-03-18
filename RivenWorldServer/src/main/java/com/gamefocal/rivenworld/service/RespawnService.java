package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.events.player.PlayerDeathEvent;
import com.gamefocal.rivenworld.events.player.PlayerRespawnEvent;
import com.gamefocal.rivenworld.game.entites.storage.DropBag;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.LinkedList;

@Singleton
@AutoService(HiveService.class)
public class RespawnService implements HiveService<ResourceService> {

    private LinkedList<Location> respawnLocations = new LinkedList<>();

    @Override
    public void init() {
        this.respawnLocations.add(Location.fromString("163080.44,89127.92,3178.2505,0.0,0.0,168.33676"));
        this.respawnLocations.add(Location.fromString("160938.14,81286.59,3131.7808,0.0,0.0,155.8364"));
        this.respawnLocations.add(Location.fromString("147308.73,23334.033,3187.0757,0.0,0.0,176.94359"));
        this.respawnLocations.add(Location.fromString("125166.55,12616.446,3186.2842,0.0,0.0,98.18861"));
        this.respawnLocations.add(Location.fromString("4179.0825,49645.586,9654.384,0.0,0.0,41.290462"));
        this.respawnLocations.add(Location.fromString("15951.87,125489.74,9927.129,0.0,0.0,-4.390228"));
        this.respawnLocations.add(Location.fromString("53057.15,162502.9,9827.326,0.0,0.0,-114.51558"));
        this.respawnLocations.add(Location.fromString("79088.39,175395.64,3299.7,0.0,0.0,-61.440887"));
        this.respawnLocations.add(Location.fromString("102494.01,167855.6,3257.1792,0.0,0.0,-98.5181"));
        this.respawnLocations.add(Location.fromString("109894.305,152321.03,3258.7302,0.0,0.0,-119.254776"));
        this.respawnLocations.add(Location.fromString("153642.66,81531.5,4281.0874,0.0,0.0,88.65631"));
    }

    public Location randomSpawnLocation() {
        return RandomUtil.getRandomElementFromList(this.respawnLocations);
    }

    public void killPlayer(HiveNetConnection connection, HiveNetConnection killedBy) {

        PlayerDeathEvent deathEvent = new PlayerDeathEvent(connection, killedBy).call();
        if (deathEvent.isCanceled()) {
            return;
        }

        connection.resetFallDamage();
        connection.setTakeFallDamage(false);

        connection.clearLookingAt();

        connection.getPlayer().playerStats.health = 0.00f;
        connection.getPlayer().playerStats.hunger = 0.00f;
        connection.getPlayer().playerStats.energy = 0.00f;
        connection.getPlayer().playerStats.thirst = 0.00f;
        connection.getState().isDead = true;
        connection.broadcastState();
        connection.sendAttributes();

//        connection.sendKillPacket();

        // Spawn inventory in bag
        if (deathEvent.isDropInventory()) {
            Inventory playerInv = connection.getPlayer().inventory;
            DropBag bag = new DropBag(connection);

            Inventory inventory = new Inventory(playerInv.getStorageSpace());
            inventory.setLocked(true);

            int i = 0;
            for (InventoryStack stack : playerInv.getItems()) {
                // TODO: Check for a soul bound tag here
                inventory.add(stack);
                playerInv.clear(i++);
            }

            bag.setInventory(inventory);
            DedicatedServer.instance.getWorld().spawn(bag, connection.getPlayer().location);
            inventory.setAttachedEntity(bag.uuid);
        }

        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.DEATH_SCREAM, connection.getPlayer().location, 250f, .5f, 1f);

        connection.sendChatMessage("" + ChatColor.BOLD + ChatColor.RED + "Death: You've been killed. You will respawn in 5 seconds.");

        // Respawn the player in seconds
        TaskService.scheduledDelayTask(() -> {

            // TODO: Select closest respawn point or random

            // Changed to closest
            Location closest = RandomUtil.getRandomElementFromList(this.respawnLocations);

            PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(connection, closest).call();
            if (respawnEvent.isCanceled()) {
                return;
            }

            connection.sendChatMessage("" + ChatColor.BOLD + ChatColor.GREEN + "Respawn: You are being respawned now.");

            connection.hide();
            connection.broadcastState();
//            connection.show();

            connection.getPlayer().playerStats.health = 100f;
            connection.getPlayer().playerStats.hunger = 100f;
            connection.getPlayer().playerStats.energy = 100f;
            connection.getPlayer().playerStats.thirst = 100f;
            connection.getState().isDead = false;
            connection.tpToLocation(respawnEvent.getRespawnLocation());
            connection.show();
            connection.broadcastState();
            connection.sendAttributes();

        }, TickUtil.SECONDS(5), false);
    }

}
