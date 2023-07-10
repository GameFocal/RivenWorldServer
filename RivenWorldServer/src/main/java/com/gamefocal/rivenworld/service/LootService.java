package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.HashMap;

@AutoService(HiveService.class)
@Singleton
public class LootService implements HiveService<LootService> {

    public static HashMap<Class<? extends InventoryItem>,Integer> lootTable = new HashMap<>();

    @Override
    public void init() {
        // TODO: Loot table
    }
}
