package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Wooden_Basin_01_Recipe;

public class Wooden_Basin_01_Item extends PlaceableInventoryItem<Wooden_Basin_01_Item> implements InventoryCraftingInterface {

    public Wooden_Basin_01_Item() {
        this.name = "Wooden Basin";
        this.desc = "A great addition to stay fancy and clean";
        this.mesh = InventoryDataRow.Wooden_Basin_01;
        this.icon = InventoryDataRow.Wooden_Basin_01;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Wooden_Basin_01_Recipe();}
}
