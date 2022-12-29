package com.gamefocal.island.game.entites;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.models.GameEntityModel;

public class EntityInteraction implements Intractable {

    private GameEntityModel model;

    public EntityInteraction(GameEntityModel model) {
        this.model = model;
    }

    public GameEntityModel getModel() {
        return model;
    }

    @Override
    public boolean canInteract(HiveNetConnection connection) {
        return true;
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action) {

    }
}
