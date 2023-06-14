package com.gamefocal.rivenworld.game.items.placables.blocks.Sand;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogRampBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Sand.SandRampBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class SandRampBlockItem extends PlaceableInventoryItem<SandRampBlockItem> implements InventoryCraftingInterface {

    public SandRampBlockItem() {
        this.name = "Sand Ramp";
        this.desc = "A ramp made of Sand";
        this.icon = InventoryDataRow.Sand_Ramp;
        this.mesh = InventoryDataRow.Sand_Ramp;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("sandramp");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new SandRampBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
