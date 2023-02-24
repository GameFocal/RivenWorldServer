package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.models.GameMetaModel;
import com.gamefocal.rivenworld.models.PlayerModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
@AutoService(HiveService.class)
public class KingService implements HiveService<KingService> {

    public static PlayerModel isTheKing;

    public ConcurrentLinkedQueue<InventoryStack> kingsTaxes = new ConcurrentLinkedQueue<>();

    private float maxItemsInTax = 125;

    private Inventory kingInventory = new Inventory(InventoryType.CONTAINER, "The Warchest", "warchest", (int) this.maxItemsInTax);

    @Override
    public void init() {

        /*
         * Load the inventory from the db
         * */
        if (GameMetaModel.hasMeta("king-warchest")) {
            String jsonInv = GameMetaModel.getMetaValue("king-warchest", null);
        }

    }


}
