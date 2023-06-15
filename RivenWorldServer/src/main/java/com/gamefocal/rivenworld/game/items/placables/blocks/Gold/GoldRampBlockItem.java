package com.gamefocal.rivenworld.game.items.placables.blocks.Gold;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassRampBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldRampBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Gold.GoldRampBlockRecipe;

public class GoldRampBlockItem extends PlaceableInventoryItem<GoldRampBlockItem> implements InventoryCraftingInterface {

    public GoldRampBlockItem() {
        this.name = "Gold Ramp";
        this.desc = "A ramp made of Gold";
        this.icon = InventoryDataRow.Gold_Ramp;
        this.mesh = InventoryDataRow.Gold_Ramp;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("goldramp");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GoldRampBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GoldRampBlockRecipe();
    }
}
