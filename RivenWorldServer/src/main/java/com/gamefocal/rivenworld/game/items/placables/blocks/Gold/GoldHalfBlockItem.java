package com.gamefocal.rivenworld.game.items.placables.blocks.Gold;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassHalfBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldHalfBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class GoldHalfBlockItem extends PlaceableInventoryItem<GoldHalfBlockItem> implements InventoryCraftingInterface {

    public GoldHalfBlockItem() {
        this.name = "Gold Half Block";
        this.desc = "A half block made of Gold";
        this.icon = InventoryDataRow.Gold_HalfBlock;
        this.mesh = InventoryDataRow.Gold_HalfBlock;
        this.placable.IsBuildingBlock = true;
        this.placable.HalfBlock = true;
        this.spawnNames.add("goldhalfblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GoldHalfBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
