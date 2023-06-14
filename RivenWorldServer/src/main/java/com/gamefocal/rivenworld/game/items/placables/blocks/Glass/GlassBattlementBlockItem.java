package com.gamefocal.rivenworld.game.items.placables.blocks.Glass;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtBattlementBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class GlassBattlementBlockItem extends PlaceableInventoryItem<GlassBattlementBlockItem> implements InventoryCraftingInterface {

    public GlassBattlementBlockItem() {
        this.name = "Glass Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.Glass_Battlement;
        this.mesh = InventoryDataRow.Glass_Battlement;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("glasscastle");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GlassBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ;
    }
}
