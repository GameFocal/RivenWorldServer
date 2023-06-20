package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.faucet_Recipe;

public class faucet_Item extends PlaceableInventoryItem<faucet_Item> implements InventoryCraftingInterface {

    public faucet_Item() {
        this.name = "Wood small Pot";
        this.desc = "Device to obtain water";
        this.mesh = InventoryDataRow.faucet;
        this.icon = InventoryDataRow.faucet;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new faucet_Recipe();}
}
