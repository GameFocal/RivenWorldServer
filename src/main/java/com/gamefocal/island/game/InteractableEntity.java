package com.gamefocal.island.game;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;

public interface InteractableEntity {

    void onInteract(HiveNetConnection connection, InteractAction action);

}
