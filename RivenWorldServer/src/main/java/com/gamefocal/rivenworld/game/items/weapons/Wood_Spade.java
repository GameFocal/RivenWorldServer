package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.recipes.weapons.WoodSpadeRecipe;

public class Wood_Spade extends ToolInventoryItem implements InventoryCraftingInterface {

    public Wood_Spade() {
        this.icon = InventoryDataRow.Wood_Spade;
        this.mesh = InventoryDataRow.Wood_Spade;
        this.tag("weapon", "oneHand");
        this.type = InventoryItemType.PRIMARY;
        this.name = "Wood Spade";
        this.desc = "A spade made of Wood that is great for digging up tree stumps";
        this.initDurability(50);
        this.spawnNames.add("woodshovel");
        this.spawnNames.add("shovel");
    }

    @Override
    public void generateUpperRightHelpText() {
        this.upperRightText.add("Can be used to dig dirt and sand nodes");
        this.upperRightText.add("[e] Use to Dig");
        super.generateUpperRightHelpText();
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public float hit() {
        return 4;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodSpadeRecipe();
    }
}
