package com.gamefocal.island.game.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.TestBlock;
import com.gamefocal.island.game.items.placables.PlaceableInventoryItem;

public class TestCube extends PlaceableInventoryItem {
    @Override
    public String slug() {
        return "test_cube";
    }

    @Override
    public void onInteract() {

    }

    @Override
    public GameEntity spawnItem() {
        return new TestBlock();
    }

    @Override
    public void onAltInteract() {

    }
}
