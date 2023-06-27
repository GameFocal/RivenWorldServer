package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Furniture_Shelf_01_Recipe;

public class Furniture_Shelf_01_Item extends PlaceableInventoryItem<Furniture_Shelf_01_Item> implements InventoryCraftingInterface {

    public Furniture_Shelf_01_Item() {
        this.name = "Shelf";
        this.desc = "A great decoration to organize";
        this.mesh = InventoryDataRow.Furniture_Shelf_01;
        this.icon = InventoryDataRow.Furniture_Shelf_01;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Furniture_Shelf_01_Recipe(); }
}
