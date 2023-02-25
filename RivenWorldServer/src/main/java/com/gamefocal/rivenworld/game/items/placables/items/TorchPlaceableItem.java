package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.TorchPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.TorchPlaceableRecipe;

public class TorchPlaceableItem extends PlaceableInventoryItem<TorchPlaceableItem> implements InventoryCraftingInterface {
    @Override
    public String slug() {
        return "TorchPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new TorchPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new TorchPlaceableRecipe();
    }
}
