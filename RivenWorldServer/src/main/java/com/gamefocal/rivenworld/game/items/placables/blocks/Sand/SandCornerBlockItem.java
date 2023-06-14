package com.gamefocal.rivenworld.game.items.placables.blocks.Sand;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Sand.SandCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class SandCornerBlockItem extends PlaceableInventoryItem<SandCornerBlockItem> implements InventoryCraftingInterface {

    public SandCornerBlockItem() {
        this.name = "Sand Corner Block";
        this.desc = "A corner block made of Sand";
        this.icon = InventoryDataRow.Sand_CornerBlock;
        this.mesh = InventoryDataRow.Sand_CornerBlock;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("sandcorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new SandCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
