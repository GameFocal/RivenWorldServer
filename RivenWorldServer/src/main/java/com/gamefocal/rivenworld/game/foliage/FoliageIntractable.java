package com.gamefocal.rivenworld.game.foliage;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.models.GameFoliageModel;

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
