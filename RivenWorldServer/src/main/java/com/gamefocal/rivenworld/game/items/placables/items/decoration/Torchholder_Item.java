package com.gamefocal.rivenworld.game.items.placables.items.decoration;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Candlelight_01_Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.Torchholder_Recipe;

public class Torchholder_Item extends PlaceableInventoryItem<Torchholder_Item> implements InventoryCraftingInterface {
    public Torchholder_Item() {
        this.name = "Torch Holder";
        this.desc = "A device or fixture for holding a torch";
        this.icon = InventoryDataRow.Torchholder;
        this.mesh = InventoryDataRow.Torchholder;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() { return null; }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new Torchholder_Recipe();
    }
}
