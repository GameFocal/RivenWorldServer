package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Metal_Chalice_03_Recipe;

public class Metal_Chalice_03_Item extends PlaceableInventoryItem<Metal_Chalice_03_Item> implements InventoryCraftingInterface {

    public Metal_Chalice_03_Item() {
        this.name = "Tall Chalice";
        this.desc = "Fancy glass for wine";
        this.mesh = InventoryDataRow.Metal_Chalice_03;
        this.icon = InventoryDataRow.Metal_Chalice_03;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Metal_Chalice_03_Recipe();}
}
