package com.gamefocal.rivenworld.game.interactable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public interface Intractable {

    boolean canInteract(HiveNetConnection connection);

    void onInteract(HiveNetConnection connection, InteractAction action);

}
