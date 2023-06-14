package com.gamefocal.rivenworld.game.items.placables.blocks.Clay;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayRampBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodRampBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.WoodRampBlockRecipe;

public class ClayRampBlockItem extends PlaceableInventoryItem<ClayRampBlockItem> implements InventoryCraftingInterface {

    public ClayRampBlockItem() {
        this.name = "Clay Ramp";
        this.desc = "A ramp made of Clay";
        this.icon = InventoryDataRow.Clay_Ramp;
        this.mesh = InventoryDataRow.Clay_Ramp;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("clayramp");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ClayRampBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
