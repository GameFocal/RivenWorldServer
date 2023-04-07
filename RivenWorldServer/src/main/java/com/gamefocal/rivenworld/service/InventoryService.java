package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.entites.storage.DropBag;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.auto.service.AutoService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.digest.DigestUtils;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@AutoService(HiveService.class)
public class InventoryService implements HiveService<InventoryService> {

    private Hashtable<UUID, Inventory> inventories = new Hashtable<>();

    private Hashtable<String, Class<? extends InventoryItem>> itemClasses = new Hashtable<>();

    private Hashtable<String, String> spawnnames = new Hashtable<>();

    public static final LinkedList<UUID> blockedSlots = new LinkedList<>();

    @Override
    public void init() {
        Set<Class<? extends InventoryItem>> inventoryItems = new Reflections("com.gamefocal").getSubTypesOf(InventoryItem.class);
        for (Class<? extends InventoryItem> cc : inventoryItems) {
            try {
                InventoryItem i = cc.newInstance();
                this.itemClasses.put(i.getClass().getSimpleName(), cc);

                // Base class
                this.spawnnames.put(i.getClass().getSimpleName(), i.getClass().getSimpleName());
                this.spawnnames.put(i.getClass().getSimpleName().toLowerCase(), i.getClass().getSimpleName());
                this.spawnnames.put(i.getClass().getSimpleName().toLowerCase().replace("_", ""), i.getClass().getSimpleName());
                this.spawnnames.put(i.getClass().getSimpleName().toLowerCase().replace("-", ""), i.getClass().getSimpleName());

                // Item Name
                if (i.getIcon() != null) {

                    // i.getClass().getSimpleName()

                    this.spawnnames.put(i.getIcon().name(), i.getClass().getSimpleName());
                    this.spawnnames.put(i.getIcon().name().toLowerCase(), i.getClass().getSimpleName());
                    this.spawnnames.put(i.getIcon().name().toLowerCase().replace("_", ""), i.getClass().getSimpleName());
                    this.spawnnames.put(i.getIcon().name().toLowerCase().replace("-", ""), i.getClass().getSimpleName());
                }

                for (String nicks : i.getSpawnNames()) {
                    this.spawnnames.put(nicks, i.getClass().getSimpleName());
                }

            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
            }
        }
    }


    public static String getSlotUID(Inventory inventory, int slot) {
        return DigestUtils.md5Hex(inventory.getUuid().toString() + ":" + slot);
    }

    public Class<? extends InventoryItem> getItemClassFromSlug(String slug) {
        if (this.itemClasses.containsKey(slug)) {
            return this.itemClasses.get(slug);
        }

        return null;
    }

    public void trackInventory(Inventory inventory) {
        this.inventories.put(inventory.getUuid(), inventory);
    }

    public void untrackInventory(Inventory inventory) {
        this.inventories.remove(inventory.getUuid());
    }

    public void untrackInventory(UUID uuid) {
        this.inventories.remove(uuid);
    }

    public Inventory getInvFromId(UUID uuid) {
        if (this.inventories.containsKey(uuid)) {
            return this.inventories.get(uuid);
        }

        return null;
    }

    public Set<String> getItemSlugs() {
        return this.itemClasses.keySet();
    }

    public Class getItemClassFromSpawnName(String name) {
        if (this.spawnnames.containsKey(name)) {
            String n = this.spawnnames.get(name);
            return this.getItemClassFromSlug(n);
        }

        return null;
    }

    public void dropBagAtLocation(HiveNetConnection connection, Inventory inventory, Location location) {
        List<DropBag> bags = DedicatedServer.instance.getWorld().getEntitesOfTypeWithinRadius(DropBag.class, location, 500);

        DropBag existingBag = null;
        if (connection != null) {
            for (DropBag b : bags) {
                if (b.getDroppedBy() == connection.getUuid()) {
                    existingBag = b;
                }
            }
        }

        if (existingBag == null) {
            // Spawn a new bag here
            DedicatedServer.instance.getWorld().spawn(new DropBag(connection, inventory.getItems()), location);
        } else {
            for (InventoryStack o : inventory.getItems()) {
                if (existingBag.getInventory().canAdd(o)) {
                    existingBag.getInventory().add(o);
                } else {
                    existingBag.getInventory().resize(1);
                    existingBag.getInventory().add(o);
                }
            }
        }
    }

    public Hashtable<UUID, Inventory> getInventories() {
        return inventories;
    }
}
