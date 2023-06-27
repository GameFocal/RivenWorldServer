package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Pottery_01_Recipe;

public class Pottery_01_Item extends PlaceableInventoryItem<Pottery_01_Item> implements InventoryCraftingInterface {

    public Pottery_01_Item() {
        this.name = "Pottery";
        this.desc = "Great addition for the decoration of your house";
        this.mesh = InventoryDataRow.Pottery_01;
        this.icon = InventoryDataRow.Pottery_01;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Pottery_01_Recipe();}
}
