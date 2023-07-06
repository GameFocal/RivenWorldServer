package com.gamefocal.rivenworld.game.items.weapons.sword;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.weapons.Sword;
import com.gamefocal.rivenworld.game.recipes.weapons.WoodenSwordRecipe;

public class WoodenSword extends Sword {

    public WoodenSword() {
        this.icon = InventoryDataRow.Wooden_Sword;
        this.mesh = InventoryDataRow.Wooden_Sword;
        this.type = InventoryItemType.PRIMARY;
        this.tag("weapon", "oneHand");
        this.name = "Wooden Short Sword";
        this.desc = "A short sword with a blade of Wood";
        this.initDurability(50);
        this.spawnNames.add("woodsword");
        this.spawnNames.add("sword");
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
        return 20;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodenSwordRecipe();
    }
}
