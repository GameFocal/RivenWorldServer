package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Metal_Cooking_Cauldron_03_Recipe;

public class Metal_Cooking_Cauldron_03_Item extends PlaceableInventoryItem<Metal_Cooking_Cauldron_03_Item> implements InventoryCraftingInterface {

    public Metal_Cooking_Cauldron_03_Item() {
        this.name = "Deep Cauldron";
        this.desc = "Great addition to help you in the preparation of food";
        this.mesh = InventoryDataRow.Metal_Cooking_Cauldron_03;
        this.icon = InventoryDataRow.Metal_Cooking_Cauldron_03;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {return null;}

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Metal_Cooking_Cauldron_03_Recipe();}
}
