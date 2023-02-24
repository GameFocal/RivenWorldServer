package com.gamefocal.rivenworld.game.items.placables.blocks;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.DirtBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DirtBlockItem extends PlaceableInventoryItem<DirtBlockItem> {
    @Override
    public String slug() {
        return "Dirt_Block";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new DirtBlock();
    }
}
