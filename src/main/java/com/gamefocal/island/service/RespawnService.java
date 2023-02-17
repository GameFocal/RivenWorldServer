package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.ChatColor;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.events.player.PlayerDeathEvent;
import com.gamefocal.island.events.player.PlayerRespawnEvent;
import com.gamefocal.island.game.entites.storage.DropBag;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.game.util.TickUtil;

import java.util.LinkedList;

public class RespawnService implements HiveService<ResourceService> {

    private LinkedList<Location> respawnLocations = new LinkedList<>();

    @Override
    public void init() {
        // Church
        this.respawnLocations.add(Location.fromString("152794.28,89038.98,3422.4011,0.0,0.0,88.958885"));

        // Shrines
        this.respawnLocations.add(Location.fromString("127407.836,108420.23,7501.81,0.0,0.0,12.571099"));
        this.respawnLocations.add(Location.fromString("69313.28,123919.16,24574.123,0.0,0.0,160.0769"));
        this.respawnLocations.add(Location.fromString("122283.195,37470.426,6431.046,0.0,0.0,152.88776"));
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

        connection.sendKillPacket();

        // Spawn inventory in bag
        if (deathEvent.isDropInventory()) {
            Inventory playerInv = connection.getPlayer().inventory;
            DropBag bag = new DropBag(connection);

            Inventory inventory = new Inventory(playerInv.getStorageSpace());
            inventory.setLocked(true);

            int i = 0;
            for (InventoryStack stack : playerInv.getItems()) {
                // TODO: Check for a sould bound tag here
                inventory.add(stack);
                playerInv.clear(i++);
            }

            bag.setInventory(inventory);
            DedicatedServer.instance.getWorld().spawn(bag, connection.getPlayer().location);
            inventory.setAttachedEntity(bag.uuid);
        }

        connection.sendChatMessage("" + ChatColor.BOLD + ChatColor.RED + "[Death]: You've been killed. You will respawn in 15 seconds.");

        // Respawn the player in seconds
        TaskService.scheduledDelayTask(() -> {

            // TODO: Select closest respawn point or random

            PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(connection, new Location(0, 0, 0)).call();
            if (respawnEvent.isCanceled()) {
                return;
            }

            connection.sendChatMessage("" + ChatColor.BOLD + ChatColor.GREEN + "[Respawn]: You are being respawned now.");
            System.out.println("Respawn.");

            // TODO: Set player state to alive and TP to repsawnLocation

        }, TickUtil.SECONDS(15), false);
    }

}
