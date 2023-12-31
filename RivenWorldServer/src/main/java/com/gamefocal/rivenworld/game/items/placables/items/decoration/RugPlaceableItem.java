package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.decoration.RugPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.RugPlaceableRecipe;

public class RugPlaceableItem extends PlaceableInventoryItem<RugPlaceableItem> implements InventoryCraftingInterface {

    public RugPlaceableItem() {
        this.name = "Rug";
        this.desc = "A decorative carpet for your house";
        this.icon = InventoryDataRow.RugPlaceable;
        this.mesh = InventoryDataRow.RugPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return new RugPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new RugPlaceableRecipe();
    }
}
