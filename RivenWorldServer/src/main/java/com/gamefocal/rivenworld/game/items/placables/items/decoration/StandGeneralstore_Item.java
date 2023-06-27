package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.StandGeneralstore_Recipe;

public class StandGeneralstore_Item extends PlaceableInventoryItem<StandGeneralstore_Item> implements InventoryCraftingInterface {

    public StandGeneralstore_Item() {
        this.name = "Stand";
        this.desc = "Great addition to decorate your store";
        this.mesh = InventoryDataRow.StandGeneralstore;
        this.icon = InventoryDataRow.StandGeneralstore;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new StandGeneralstore_Recipe();}
}
