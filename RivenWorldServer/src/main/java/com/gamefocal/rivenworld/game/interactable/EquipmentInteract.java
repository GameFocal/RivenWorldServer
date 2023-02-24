package com.gamefocal.rivenworld.game.interactable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public interface EquipmentInteract<T> {

    void onEquipmentInteract(T interact, HiveNetConnection connection, InteractAction action);

}
