package com.gamefocal.rivenworld.game.items.placables.blocks.Glass;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Glass.GlassCornerBlockRecipe;

public class GlassCornerBlockItem extends PlaceableInventoryItem<GlassCornerBlockItem> implements InventoryCraftingInterface {

    public GlassCornerBlockItem() {
        this.name = "Glass Corner Block";
        this.desc = "A corner block made of Glass";
        this.icon = InventoryDataRow.Glass_CornerBlock;
        this.mesh = InventoryDataRow.Glass_CornerBlock;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("glasscorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GlassCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GlassCornerBlockRecipe();
    }
}
