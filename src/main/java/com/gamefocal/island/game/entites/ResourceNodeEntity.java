package com.gamefocal.island.game.entites;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.inventory.InventoryStack;

public class ResourceNodeEntity<A> extends GameEntity<A> {

    public float health = 100f;

    public float maxHealth = 100f;

    public InventoryStack[] drops = new InventoryStack[0];

}