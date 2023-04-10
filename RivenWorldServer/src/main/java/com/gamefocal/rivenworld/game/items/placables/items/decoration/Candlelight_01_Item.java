package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.decoration.BedPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.BedPlaceableRecipe;

public class Candlelight_01_Item extends PlaceableInventoryItem<Candlelight_01_Item> implements InventoryCraftingInterface {
    public Candlelight_01_Item() {
        this.name = "Tall Table Candle Stick";
        this.desc = "A candle that can be lit";
        this.icon = InventoryDataRow.Candlelight_01;
        this.mesh = InventoryDataRow.Candlelight_01;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
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