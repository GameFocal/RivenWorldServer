package com.gamefocal.rivenworld.game.entites;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.models.GameEntityModel;

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
