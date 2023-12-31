package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.decoration.TablePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.TablePlaceableRecipe;

public class TablePlaceableItem extends PlaceableInventoryItem<TablePlaceableItem> implements InventoryCraftingInterface {

    public TablePlaceableItem() {
        this.name = "Wooden Table";
        this.desc = "A great decoration for your house";
        this.icon = InventoryDataRow.TablePlaceable;
        this.mesh = InventoryDataRow.TablePlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = false;
        this.spawnNames.add("table");
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
