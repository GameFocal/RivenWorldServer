package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.TablePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.TablePlaceableRecipe;

public class TablePlaceableItem extends PlaceableInventoryItem<TablePlaceableItem> implements InventoryCraftingInterface {

    public TablePlaceableItem() {
        this.icon = InventoryDataRow.TablePlaceable;
        this.mesh = InventoryDataRow.TablePlaceable;
    }

    @Override
    public GameEntity spawnItem() {
        return new TablePlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new TablePlaceableRecipe();
    }
}
