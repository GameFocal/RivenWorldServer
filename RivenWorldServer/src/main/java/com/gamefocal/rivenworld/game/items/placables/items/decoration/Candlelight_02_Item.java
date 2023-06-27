package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.decoration.ChairPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Candlelight_02_Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.ChairPlaceableRecipe;

public class Candlelight_02_Item extends PlaceableInventoryItem<Candlelight_02_Item> implements InventoryCraftingInterface {

    public Candlelight_02_Item() {
        this.name = "Small Table Candle Stick";
        this.desc = "A candle that can be lit";
        this.mesh = InventoryDataRow.Candlelight_02;
        this.icon = InventoryDataRow.Candlelight_02;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.DetectCollision = false;
    }

    @Override
    public GameEntity spawnItem() { return null;}

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) { return new Candlelight_02_Recipe(); }
}
