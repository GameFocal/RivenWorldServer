package com.gamefocal.island.game.interactable;

import com.gamefocal.island.entites.net.HiveNetConnection;

public interface EquipmentInteract<T> {

    void onEquipmentInteract(T interact, HiveNetConnection connection, InteractAction action);

}
