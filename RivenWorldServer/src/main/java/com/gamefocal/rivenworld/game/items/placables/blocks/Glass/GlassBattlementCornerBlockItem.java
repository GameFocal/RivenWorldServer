package com.gamefocal.rivenworld.game.items.placables.blocks.Glass;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtBattlementCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class GlassBattlementCornerBlockItem extends PlaceableInventoryItem<GlassBattlementCornerBlockItem> implements InventoryCraftingInterface {

    public GlassBattlementCornerBlockItem() {
        this.name = "Glass Corner Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.Glass_BattlementCorner;
        this.mesh = InventoryDataRow.Glass_BattlementCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("glasscastlecorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GlassBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
