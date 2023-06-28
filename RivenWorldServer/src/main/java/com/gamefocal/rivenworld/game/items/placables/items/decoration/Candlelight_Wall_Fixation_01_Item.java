package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Candlelight_Wall_Fixation_01_Recipe;

public class Candlelight_Wall_Fixation_01_Item extends PlaceableInventoryItem<Candlelight_Wall_Fixation_01_Item> implements InventoryCraftingInterface {

    public Candlelight_Wall_Fixation_01_Item() {
        this.name = "Wall Candle";
        this.desc = "A candle that can be lit";
        this.mesh = InventoryDataRow.Candlelight_Wall_Fixation_01;
        this.icon = InventoryDataRow.Candlelight_Wall_Fixation_01;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new Candlelight_Wall_Fixation_01_Recipe();
    }
}
