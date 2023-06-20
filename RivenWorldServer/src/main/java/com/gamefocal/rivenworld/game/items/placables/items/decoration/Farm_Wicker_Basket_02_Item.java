package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Farm_Wicker_Basket_02_Recipe;

public class Farm_Wicker_Basket_02_Item extends PlaceableInventoryItem<Farm_Wicker_Basket_02_Item> implements InventoryCraftingInterface {

    public Farm_Wicker_Basket_02_Item() {
        this.name = " Little Round Basket";
        this.desc = "A great addition to the daily day";
        this.mesh = InventoryDataRow.Farm_Wicker_Basket_02;
        this.icon = InventoryDataRow.Farm_Wicker_Basket_02;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Farm_Wicker_Basket_02_Recipe();}
}
