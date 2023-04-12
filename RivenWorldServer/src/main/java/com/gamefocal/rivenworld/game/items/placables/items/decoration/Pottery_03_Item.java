package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Pottery_03_Recipe;

public class Pottery_03_Item extends PlaceableInventoryItem<Pottery_03_Item> implements InventoryCraftingInterface {

    public Pottery_03_Item() {
        this.name = "Big ceramic pot";
        this.desc = "Great addition for the decoration of your house";
        this.mesh = InventoryDataRow.Pottery_03;
        this.icon = InventoryDataRow.Pottery_03;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Pottery_03_Recipe();}
}
