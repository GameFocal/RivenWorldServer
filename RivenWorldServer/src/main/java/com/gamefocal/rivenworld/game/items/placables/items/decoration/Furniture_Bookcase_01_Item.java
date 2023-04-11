package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.decoration.TablePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Furniture_Bookcase_01_Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.TablePlaceableRecipe;

public class Furniture_Bookcase_01_Item extends PlaceableInventoryItem<Furniture_Bookcase_01_Item> implements InventoryCraftingInterface {

    public Furniture_Bookcase_01_Item() {
        this.name = "Bookcase";
        this.desc = "The perfect place to put your books";
        this.icon = InventoryDataRow.Furniture_Bookcase_01;
        this.mesh = InventoryDataRow.Furniture_Bookcase_01;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new Furniture_Bookcase_01_Recipe();
    }
}
