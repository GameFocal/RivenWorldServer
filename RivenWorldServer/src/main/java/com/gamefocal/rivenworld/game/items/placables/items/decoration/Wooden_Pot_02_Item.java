package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Wooden_Pot_02_Recipe;

public class Wooden_Pot_02_Item extends PlaceableInventoryItem<Wooden_Pot_02_Item> implements InventoryCraftingInterface {

    public Wooden_Pot_02_Item() {
        this.name = "Wood medium Pot";
        this.desc = "Great addition to decorate";
        this.mesh = InventoryDataRow.Wooden_Pot_02;
        this.icon = InventoryDataRow.Wooden_Pot_02;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Wooden_Pot_02_Recipe();}
}
