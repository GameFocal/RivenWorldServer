package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Furniture_Counter_Corner_01_Recipe;

public class Furniture_Counter_Corner_01_Item extends PlaceableInventoryItem<Furniture_Counter_Corner_01_Item> implements InventoryCraftingInterface {

    public Furniture_Counter_Corner_01_Item() {
        this.name = "Counter Corner";
        this.desc = "A great decoration for your store";
        this.mesh = InventoryDataRow.Furniture_Counter_Corner_01;
        this.icon = InventoryDataRow.Furniture_Counter_Corner_01;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new Furniture_Counter_Corner_01_Recipe();
    }
}
