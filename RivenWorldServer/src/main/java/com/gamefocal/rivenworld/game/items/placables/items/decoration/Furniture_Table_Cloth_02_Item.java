package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Furniture_Table_Cloth_02_Recipe;

public class Furniture_Table_Cloth_02_Item extends PlaceableInventoryItem<Furniture_Table_Cloth_02_Item> implements InventoryCraftingInterface {

    public Furniture_Table_Cloth_02_Item() {
        this.name = "Blue Rectangle Table Cloth";
        this.desc = "A great addition to decorate your tables";
        this.mesh = InventoryDataRow.Furniture_Table_Cloth_02;
        this.icon = InventoryDataRow.Furniture_Table_Cloth_02;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Furniture_Table_Cloth_02_Recipe();}
}
