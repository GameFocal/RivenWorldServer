package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.google.auto.service.AutoService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Singleton
@AutoService(HiveService.class)
public class InventoryService implements HiveService<InventoryService> {

    private Hashtable<UUID, Inventory> inventories = new Hashtable<>();

    private Hashtable<String, Class<? extends InventoryItem>> itemClasses = new Hashtable<>();

    private Hashtable<String,String> spawnnames = new Hashtable<>();

    @Override
    public void init() {
        Set<Class<? extends InventoryItem>> inventoryItems = new Reflections("com.gamefocal").getSubTypesOf(InventoryItem.class);
        for (Class<? extends InventoryItem> cc : inventoryItems) {
            try {
                InventoryItem i = cc.newInstance();

                String slug = i.slug();

//                System.out.println(slug);

                this.itemClasses.put(slug, cc);

                spawnnames.put(slug,slug);

            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
            }
        }

        // Load in the items.json
        InputStream s = getClass().getClassLoader().getResourceAsStream("items.json");
        try {
            JsonObject object = JsonParser.parseString(new String(s.readAllBytes())).getAsJsonObject();

            for (Map.Entry<String, JsonElement> e : object.entrySet()) {
                String spawnName = e.getKey();
                JsonArray al = e.getValue().getAsJsonObject().get("aliases").getAsJsonArray();

//                spawnnames.put(spawnName,spawnName);

                for (JsonElement a : al) {
                    String aaa = a.getAsString();
                    spawnnames.put(aaa,spawnName);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
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

    public Set<String> getItemSlugs() {
        return this.itemClasses.keySet();
    }

    public Class getItemClassFromSpawnName(String name) {
        if(this.spawnnames.containsKey(name)) {
            String n = this.spawnnames.get(name);
            return this.getItemClassFromSlug(n);
        }

        return null;
    }

    public Hashtable<UUID, Inventory> getInventories() {
        return inventories;
    }
}
