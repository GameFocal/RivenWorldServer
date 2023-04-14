package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.lights.TorchPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.TorchPlaceableRecipe;

public class TorchPlaceableItem extends PlaceableInventoryItem<TorchPlaceableItem> implements InventoryCraftingInterface {

    public TorchPlaceableItem() {
        this.name = "Wall Torch";
        this.desc = "A torch that attaches to a wall";
        this.icon = InventoryDataRow.TorchPlaceable;
        this.mesh = InventoryDataRow.TorchPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.DetectCollision = true;
        this.placable.BaseType = 2;
        this.placable.SnaptoBase = true;
        this.spawnNames.add("walltorch");
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
