package com.gamefocal.rivenworld.game.items.placables.blocks.Gold;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassRoundCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldRoundCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class GoldRoundCornerBlockItem extends PlaceableInventoryItem<GoldRoundCornerBlockItem> implements InventoryCraftingInterface {

    public GoldRoundCornerBlockItem() {
        this.name = "Gold Round corner Block";
        this.desc = "A block made of Gold";
        this.icon = InventoryDataRow.Gold_RoundCorner;
        this.mesh = InventoryDataRow.Gold_RoundCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("goldroundcornerblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GoldRoundCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
