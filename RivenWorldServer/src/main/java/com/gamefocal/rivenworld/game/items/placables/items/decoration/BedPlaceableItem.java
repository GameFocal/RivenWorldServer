package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.decoration.BedPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.BedPlaceableRecipe;

public class BedPlaceableItem extends PlaceableInventoryItem<BedPlaceableItem> implements InventoryCraftingInterface {
    public BedPlaceableItem() {
        this.name = "Wooden Bed";
        this.desc = "A place to sleep and respawn if you die";
        this.icon = InventoryDataRow.bedPlaceable;
        this.mesh = InventoryDataRow.bedPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
        this.spawnNames.add("bed");
    }

    @Override
    public GameEntity spawnItem() {
        return new BedPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new BedPlaceableRecipe();
    }
}
