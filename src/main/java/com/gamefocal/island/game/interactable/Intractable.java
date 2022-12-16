package com.gamefocal.island.game.interactable;

import com.gamefocal.island.entites.net.HiveNetConnection;

public interface Intractable {

    boolean canInteract(HiveNetConnection connection);

    void onInteract(HiveNetConnection connection, InteractAction action);

}
