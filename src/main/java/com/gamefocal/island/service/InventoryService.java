package com.gamefocal.island.service;

import com.gamefocal.island.entites.net.HiveCommand;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.google.auto.service.AutoService;
import com.j256.ormlite.stmt.query.In;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;

@Singleton
@AutoService(HiveService.class)
public class InventoryService implements HiveService<InventoryService> {

    private Hashtable<UUID, Inventory> inventories = new Hashtable<>();

    private Hashtable<String, Class<? extends InventoryItem>> itemClasses = new Hashtable<>();

    @Override
    public void init() {
        Set<Class<? extends InventoryItem>> inventoryItems = new Reflections("com.gamefocal").getSubTypesOf(InventoryItem.class);
        for (Class<? extends InventoryItem> cc : inventoryItems) {
            try {
                InventoryItem i = cc.newInstance();

                String slug = i.slug();

                System.out.println(slug);

                this.itemClasses.put(slug, cc);

            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
            }
        }
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

    public Hashtable<UUID, Inventory> getInventories() {
        return inventories;
    }
}
