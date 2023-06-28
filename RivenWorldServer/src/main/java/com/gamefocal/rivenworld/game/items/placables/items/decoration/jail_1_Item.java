package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.decoration.jail_1;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Wooden_Pot_03_Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.jail_1_Recipe;

public class jail_1_Item extends PlaceableInventoryItem<jail_1_Item> implements InventoryCraftingInterface {

    public jail_1_Item() {
        this.name = "Jail";
        this.desc = "Great addition to put your prisoners";
        this.mesh = InventoryDataRow.jail_1;
        this.icon = InventoryDataRow.jail_1;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = false;
        this.spawnNames.add("bigjail");
    }

    @Override
    public GameEntity spawnItem() {
        return new jail_1();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new jail_1_Recipe();}
}
