package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.decoration.jail_3;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Wooden_Pot_03_Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.jail_3_Recipe;

public class


jail_3_Item extends PlaceableInventoryItem<jail_3_Item> implements InventoryCraftingInterface {

    public jail_3_Item() {
        this.name = "Simple Jail";
        this.desc = "Great addition to put your prisoners";
        this.mesh = InventoryDataRow.jail_3;
        this.icon = InventoryDataRow.jail_3;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new jail_3();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new jail_3_Recipe();}
}
