package com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.StoneBrick.StoneBrickRampBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick.StoneBrickRampBlockRecipe;

public class StoneBrickRampBlockItem extends PlaceableInventoryItem<StoneBrickRampBlockItem> implements InventoryCraftingInterface {

    public StoneBrickRampBlockItem() {
        this.name = "Stone Brick Ramp";
        this.desc = "A ramp made of stone bricks";
        this.icon = InventoryDataRow.StoneBrickRamp_Block;
        this.mesh = InventoryDataRow.StoneBrickRamp_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonebrickramp");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrickRampBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBrickRampBlockRecipe();
    }
}
