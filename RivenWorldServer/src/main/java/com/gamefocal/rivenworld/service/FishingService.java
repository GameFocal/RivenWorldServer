package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.Hashtable;

@Singleton
@AutoService(HiveService.class)
public class FishingService implements HiveService<FishingService> {

    private Hashtable<InventoryItem, Float> fish = new Hashtable<>();

    @Override
    public void init() {
        // TODO: Add fish types here and spawn them into the inventory based on chance.
    }

    public InventoryItem attemptCatchFish(HiveNetConnection connection) {
        if (connection.getPlayer().isFishing()) {
            // TODO: Attempt the catch here.
        }

        return null;
    }

    public Hashtable<InventoryItem, Float> getFish() {
        return fish;
    }

    public void setFish(Hashtable<InventoryItem, Float> fish) {
        this.fish = fish;
    }
}
