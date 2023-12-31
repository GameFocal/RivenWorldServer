package com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneRoundCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.StoneBrick.StoneBrickRoundCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick.StoneBrickRoundCornerBlockRecipe;

public class StoneBrickRoundCornerBlockItem extends PlaceableInventoryItem<StoneBrickRoundCornerBlockItem> implements InventoryCraftingInterface {

    public StoneBrickRoundCornerBlockItem() {
        this.name = "StoneBrick Round corner Block";
        this.desc = "A block made of StoneBrick";
        this.icon = InventoryDataRow.StoneBrick_RoundCorner;
        this.mesh = InventoryDataRow.StoneBrick_RoundCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonebrickroundcornerblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrickRoundCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBrickRoundCornerBlockRecipe();
    }
}
