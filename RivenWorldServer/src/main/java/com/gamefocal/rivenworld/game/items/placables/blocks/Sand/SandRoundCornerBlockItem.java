package com.gamefocal.rivenworld.game.items.placables.blocks.Sand;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogRoundCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Sand.SandRoundCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Sand.SandRoundCornerBlockRecipe;

public class SandRoundCornerBlockItem extends PlaceableInventoryItem<SandRoundCornerBlockItem> implements InventoryCraftingInterface {

    public SandRoundCornerBlockItem() {
        this.name = "Sand Round corner Block";
        this.desc = "A block made of Sand";
        this.icon = InventoryDataRow.Sand_RoundCorner;
        this.mesh = InventoryDataRow.Sand_RoundCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("sandroundcornerblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new SandRoundCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SandRoundCornerBlockRecipe();
    }
}
