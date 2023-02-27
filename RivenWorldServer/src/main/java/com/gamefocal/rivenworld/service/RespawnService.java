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

        this.respawnLocations.add(Location.fromString("136270.14,107981.97,7607.8086,0.0,0.0,-148.06482"));
        this.respawnLocations.add(Location.fromString("161987.05,94237.06,3572.62,0.0,0.0,-149.54297"));
        this.respawnLocations.add(Location.fromString("113930.07,144062.56,3434.4236,0.0,0.0,-135.3729"));
        this.respawnLocations.add(Location.fromString("21664.385,126123.25,10106.042,0.0,0.0,-83.57004"));
        this.respawnLocations.add(Location.fromString("-491.70294,100072.16,9208.828,0.0,0.0,4.353041"));
        this.respawnLocations.add(Location.fromString("55720.367,6729.416,3253.1008,0.0,0.0,31.188532"));

//        // Church
//        this.respawnLocations.add(Location.fromString("152794.28,89038.98,3422.4011,0.0,0.0,88.958885"));
//
//        // Shrines
//        this.respawnLocations.add(Location.fromString("127407.836,108420.23,7501.81,0.0,0.0,12.571099"));
//        this.respawnLocations.add(Location.fromString("69313.28,123919.16,24574.123,0.0,0.0,160.0769"));
//        this.respawnLocations.add(Location.fromString("122283.195,37470.426,6431.046,0.0,0.0,152.88776"));
    }

    public Location randomSpawnLocation() {
        return RandomUtil.getRandomElementFromList(this.respawnLocations);
    }

    public void killPlayer(HiveNetConnection connection, HiveNetConnection killedBy) {

        PlayerDeathEvent deathEvent = new PlayerDeathEvent(connection, killedBy).call();
        if (deathEvent.isCanceled()) {
            return;
        }

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
