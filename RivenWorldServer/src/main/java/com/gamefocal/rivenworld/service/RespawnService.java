package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.events.player.PlayerDeathEvent;
import com.gamefocal.rivenworld.events.player.PlayerRespawnEvent;
import com.gamefocal.rivenworld.game.entites.placable.decoration.BedPlaceable;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.player.AnimSlot;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.LinkedList;

@Singleton
@AutoService(HiveService.class)
public class RespawnService implements HiveService<ResourceService> {

    private LinkedList<Location> respawnLocations = new LinkedList<>();

    @Override
    public void init() {

        if (DedicatedServer.settings.spawnPoints.size() > 0) {
            for (String l : DedicatedServer.settings.spawnPoints) {
                this.respawnLocations.add(Location.fromString(l));
            }
        } else {

//        this.respawnLocations.addAll(DedicatedServer.settings.spawnPoints)
//            this.respawnLocations.add(Location.fromString("163080.44,89127.92,3178.2505,0.0,0.0,168.33676"));
//            this.respawnLocations.add(Location.fromString("160938.14,81286.59,3131.7808,0.0,0.0,155.8364"));
//            this.respawnLocations.add(Location.fromString("147308.73,23334.033,3187.0757,0.0,0.0,176.94359"));
//            this.respawnLocations.add(Location.fromString("125166.55,12616.446,3186.2842,0.0,0.0,98.18861"));
//            this.respawnLocations.add(Location.fromString("4179.0825,49645.586,9654.384,0.0,0.0,41.290462"));
//            this.respawnLocations.add(Location.fromString("15951.87,125489.74,9927.129,0.0,0.0,-4.390228"));
//            this.respawnLocations.add(Location.fromString("53057.15,162502.9,9827.326,0.0,0.0,-114.51558"));
//            this.respawnLocations.add(Location.fromString("79088.39,175395.64,3299.7,0.0,0.0,-61.440887"));
//            this.respawnLocations.add(Location.fromString("102494.01,167855.6,3257.1792,0.0,0.0,-98.5181"));
//            this.respawnLocations.add(Location.fromString("109894.305,152321.03,3258.7302,0.0,0.0,-119.254776"));
//            this.respawnLocations.add(Location.fromString("153642.66,81531.5,4281.0874,0.0,0.0,88.65631"));

            /*
             * Change these to be around paths and lower the spawn amount TODO: ZP
             * */
            this.respawnLocations.add(Location.fromString("102413.016,167491.61,3252.9937,0.0,0.0,-95.892395"));
            this.respawnLocations.add(Location.fromString("111154.98,154966.17,3199.991,0.0,0.0,-122.92436"));
            this.respawnLocations.add(Location.fromString("116849.25,146552.98,3467.4927,0.0,0.0,-41.577576"));
            this.respawnLocations.add(Location.fromString("152470.23,94872.14,3574.315,0.0,0.0,-75.5636"));
            this.respawnLocations.add(Location.fromString("161279.45,93926.31,3566.5125,0.0,0.0,-152.70624"));
            this.respawnLocations.add(Location.fromString("164218.05,89976.61,3173.4463,0.0,0.0,151.40413"));
            this.respawnLocations.add(Location.fromString("163124.16,83955.85,3160.1267,0.0,0.0,130.14854"));
            this.respawnLocations.add(Location.fromString("155155.69,66965.87,3437.4307,0.0,0.0,113.33487"));


        }
    }

    public Location randomSpawnLocation() {
        if (DedicatedServer.settings.randomSpawn) {
            return RandomUtil.getRandomElementFromList(this.respawnLocations);
        } else {
            return this.respawnLocations.get(0);
        }
    }

    public void killPlayer(HiveNetConnection connection, HiveNetConnection killedBy) {

        PlayerDeathEvent deathEvent = new PlayerDeathEvent(connection, killedBy).call();
        if (deathEvent.isCanceled()) {
            return;
        }

        connection.resetFallDamage();
        connection.setTakeFallDamage(false);

        connection.clearLookingAt();
        connection.disableMovment();
        connection.disableLook();

        connection.getPlayer().playerStats.health = 0.00f;
        connection.getPlayer().playerStats.hunger = 0.00f;
        connection.getPlayer().playerStats.energy = 0.00f;
        connection.getPlayer().playerStats.thirst = 0.00f;
        connection.getState().isDead = true;
        connection.broadcastState();
        connection.sendAttributes();

//        connection.sendKillPacket();

        // Spawn inventory in bag
        if (deathEvent.isDropInventory() && DedicatedServer.settings.dropInventoryOnDeath) {
            Inventory playerInv = connection.getPlayer().inventory;
//            DropBag bag = new DropBag(connection);

            Inventory inventory = new Inventory(playerInv.getStorageSpace() + 6);
            inventory.setLocked(true);

            int i = 0;
            for (InventoryStack stack : playerInv.getItems()) {
                // TODO: Check for a soul bound tag here
                if (stack != null) {
                    inventory.add(stack);
                    playerInv.clear(i++);
                }
            }

            if (DedicatedServer.settings.dropArmorOnDeath) {
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot != null) {
                        inventory.add(connection.getPlayer().equipmentSlots.getFromSlotName(slot));
                        connection.getPlayer().equipmentSlots.setBySlotName(slot, null);
                    }
                }
            }

//            bag.setInventory(inventory);

            DedicatedServer.get(InventoryService.class).dropBagAtLocation(connection, inventory, connection.getPlayer().location, false);

//            DedicatedServer.instance.getWorld().spawn(bag, connection.getPlayer().location);
//            inventory.setAttachedEntity(bag.uuid);
        }

        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.DEATH_SCREAM, connection.getPlayer().location, 250f, .5f, 1f);

        connection.sendChatMessage("" + ChatColor.BOLD + ChatColor.RED + "Death: You've been killed. You will respawn shortly.");

        connection.setAnimationCallback((connection1, args) -> {
            Location closest = RandomUtil.getRandomElementFromList(this.respawnLocations);

            BedPlaceable respawnBed = connection.getRespawnBed();
            if (respawnBed != null) {
                connection.sendChatMessage("" + ChatColor.BOLD + ChatColor.GREEN + "Respawn: Sending you to your bed.");
                closest = respawnBed.location.cpy().addZ(300);
            }

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
            connection.setCaptured(false);
            connection.show();
            connection.broadcastState();
            connection.sendAttributes();
            connection.updatePlayerInventory();
            connection.syncEquipmentSlots();
            connection.enableMovment();
            connection.enableLook();
            connection.clearAllEffects();
            connection.clearScreenEffect();
        });
        connection.playAnimation(Animation.DieForward, AnimSlot.DefaultSlot, 1, 0, -1, .25f, .25f, true);
        connection.broadcastState();
        connection.sendAttributes();
    }

}
