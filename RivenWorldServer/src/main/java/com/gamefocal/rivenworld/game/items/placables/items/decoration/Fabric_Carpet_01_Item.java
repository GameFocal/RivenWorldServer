package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.decoration.RugPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Fabric_Carpet_01_Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.RugPlaceableRecipe;

public class Fabric_Carpet_01_Item extends PlaceableInventoryItem<Fabric_Carpet_01_Item> implements InventoryCraftingInterface {

    public Fabric_Carpet_01_Item() {
        this.name = "Rug decor";
        this.desc = "A decorative carpet for your house";
        this.icon = InventoryDataRow.Fabric_Carpet_01;
        this.mesh = InventoryDataRow.Fabric_Carpet_01;
        this.placable.IsPlacableEntity = true;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return null ;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new Fabric_Carpet_01_Recipe();
    }
}
