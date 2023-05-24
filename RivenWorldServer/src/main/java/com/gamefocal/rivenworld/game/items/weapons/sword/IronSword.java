package com.gamefocal.rivenworld.game.items.weapons.sword;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.weapons.Sword;
import com.gamefocal.rivenworld.game.recipes.weapons.IronSwordRecipe;

public class IronSword extends Sword {

    public IronSword() {
        this.icon = InventoryDataRow.Iron_Sword;
        this.mesh = InventoryDataRow.Iron_Sword;
        this.type = InventoryItemType.PRIMARY;
        this.tag("weapon", "oneHand");
        this.name = "Iron Short Sword";
        this.desc = "A short sword with a blade of Iron";
        this.initDurability(200);
        this.spawnNames.add("ironsword");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public float hit() {
        return 5;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new IronSwordRecipe();
    }
}
