package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.Gate1Placeable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.GatePlaceableRecipe;

public class Gate1PlaceableItem extends PlaceableInventoryItem<Gate1PlaceableItem> implements InventoryCraftingInterface {

    public Gate1PlaceableItem() {
        this.name = "Gate";
        this.desc = "A big gate to keep enemies outside your territory.";
        this.icon = InventoryDataRow.Gate1Placeable;
        this.mesh = InventoryDataRow.Gate1Placeable;
        this.placable.IsPlacableEntity = true;
        this.placable.TerrainBig = true;
        this.placable.CustomPlaceable = true;
        this.spawnNames.add("gate");
    }

    @Override
    public GameEntity spawnItem() {
        return new Gate1Placeable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GatePlaceableRecipe();
    }
}
