package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.Target_Entity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Furniture_Table_Round_01_Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Target_Recipe;

public class Target_Item extends PlaceableInventoryItem<Target_Item> implements InventoryCraftingInterface {

    public Target_Item() {
        this.name = "Archery Target";
        this.desc = "A object to practice archery";
        this.mesh = InventoryDataRow.Target;
        this.icon = InventoryDataRow.Target;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return new Target_Entity();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Target_Recipe();}
}
