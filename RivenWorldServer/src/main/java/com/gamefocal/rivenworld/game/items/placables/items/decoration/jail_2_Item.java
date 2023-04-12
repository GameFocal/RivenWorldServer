package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Wooden_Pot_03_Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.jail_2_Recipe;

public class jail_2_Item extends PlaceableInventoryItem<jail_2_Item> implements InventoryCraftingInterface {

    public jail_2_Item() {
        this.name = "Car Jail";
        this.desc = "Great addition to put your prisoners and translate";
        this.mesh = InventoryDataRow.jail_2;
        this.icon = InventoryDataRow.jail_2;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new jail_2_Recipe();}
}
