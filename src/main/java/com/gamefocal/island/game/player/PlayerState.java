package com.gamefocal.island.game.player;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.util.Location;

import java.util.UUID;

public class PlayerState {

    public transient HiveNetConnection player;

    public String hash;

    public String animation = null;

    public Location location = null;

    public boolean isSpeaking = false;

    public boolean isSwimming = false;

    public InventoryItem inHand = null;

}
