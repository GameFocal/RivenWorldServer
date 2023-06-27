package com.gamefocal.rivenworld.game.items.placables.blocks.Gold;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Gold.GoldCornerBlockRecipe;

public class GoldCornerBlockItem extends PlaceableInventoryItem<GoldCornerBlockItem> implements InventoryCraftingInterface {

    public GoldCornerBlockItem() {
        this.name = "Gold Corner Block";
        this.desc = "A corner block made of Gold";
        this.icon = InventoryDataRow.Gold_CornerBlock;
        this.mesh = InventoryDataRow.Gold_CornerBlock;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("goldcorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GoldCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GoldCornerBlockRecipe();
    }
}
