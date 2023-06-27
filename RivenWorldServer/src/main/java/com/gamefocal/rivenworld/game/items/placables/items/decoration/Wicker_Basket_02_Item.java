package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Wicker_Basket_02_Recipe;

public class Wicker_Basket_02_Item extends PlaceableInventoryItem<Wicker_Basket_02_Item> implements InventoryCraftingInterface {

    public Wicker_Basket_02_Item() {
        this.name = "Rustic small Basket";
        this.desc = "A great addition to your house";
        this.mesh = InventoryDataRow.Wicker_Basket_02;
        this.icon = InventoryDataRow.Wicker_Basket_02;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Wicker_Basket_02_Recipe();}
}
