package com.gamefocal.rivenworld.game.items.placables.blocks.Glass;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtRampBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassRampBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class GlassRampBlockItem extends PlaceableInventoryItem<GlassRampBlockItem> implements InventoryCraftingInterface {

    public GlassRampBlockItem() {
        this.name = "Glass Ramp";
        this.desc = "A ramp made of Glass";
        this.icon = InventoryDataRow.Glass_Ramp;
        this.mesh = InventoryDataRow.Glass_Ramp;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("glassramp");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GlassRampBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
