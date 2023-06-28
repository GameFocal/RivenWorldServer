package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Bowl_Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Wooden_Pot_03_Recipe;

public class Bowl_Item extends PlaceableInventoryItem<Bowl_Item> implements InventoryCraftingInterface {

    public Bowl_Item() {
        this.name = "Rock Bowl";
        this.desc = "Great addition to decorate";
        this.mesh = InventoryDataRow.Bowl;
        this.icon = InventoryDataRow.Bowl;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {return null;}

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Bowl_Recipe();}
}
