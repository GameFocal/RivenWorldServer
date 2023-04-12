package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Metal_Ladle_01_Recipe;

public class Metal_Ladle_01_Item extends PlaceableInventoryItem<Metal_Ladle_01_Item> implements InventoryCraftingInterface {

    public Metal_Ladle_01_Item() {
        this.name = "Landle";
        this.desc = "You need it or you will burn your hands serving the soup";
        this.mesh = InventoryDataRow.Metal_Ladle_01;
        this.icon = InventoryDataRow.Metal_Ladle_01;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Metal_Ladle_01_Recipe();}
}
