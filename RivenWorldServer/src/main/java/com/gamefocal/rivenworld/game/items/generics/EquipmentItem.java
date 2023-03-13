package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public interface EquipmentItem {

    boolean canEquip(HiveNetConnection connection);

    String toSocket();

    void onEquip(HiveNetConnection connection);

    void onUnequipped(HiveNetConnection connection);

}
