package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.DoorPlaceable3;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.DoorPlaceable3Recipe;

public class DoorPlaceable3Item extends PlaceableInventoryItem<DoorPlaceable3Item> implements InventoryCraftingInterface {

    public DoorPlaceable3Item() {
        this.icon = InventoryDataRow.DoorPlaceable3;
        this.mesh = InventoryDataRow.DoorPlaceable3;
    }

    @Override
    public GameEntity spawnItem() {
        return new DoorPlaceable3();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new DoorPlaceable3Recipe();
    }
}
