package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.recipes.weapons.SpadeRecipe;

public class Spade extends ToolInventoryItem implements InventoryCraftingInterface {

    public Spade() {
        this.icon = InventoryDataRow.Spade;
        this.mesh = InventoryDataRow.Spade;
        this.type = InventoryItemType.PRIMARY;
        this.tag("weapon", "oneHand");
        this.name = "Iron Spade";
        this.desc = "A spade made of Iron that is great for digging up tree stumps";
        this.initDurability(100);
        this.spawnNames.add("ironshovel");
    }

    @Override
    public void generateUpperRightHelpText() {
        this.upperRightText.add("Can be used to dig dirt and sand nodes");
        super.generateUpperRightHelpText();
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public float hit() {
        return 2;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SpadeRecipe();
    }
}
