package com.gamefocal.island.game.foliage;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.foliage.FoliageState;
import com.gamefocal.island.game.interactable.EquipmentInteract;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.FoliageService;

public class FoliageIntractable implements Intractable {

    private GameFoliageModel foliageModel;

    public FoliageIntractable(GameFoliageModel foliageModel) {
        this.foliageModel = foliageModel;
    }

    @Override
    public boolean canInteract(HiveNetConnection connection) {
        return this.foliageModel.foliageState == FoliageState.GROWN && this.foliageModel.health > 0;
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action) {
    }

    public GameFoliageModel getFoliageModel() {
        return foliageModel;
    }
}
