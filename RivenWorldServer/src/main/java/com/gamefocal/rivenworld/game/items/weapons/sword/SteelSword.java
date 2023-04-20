package com.gamefocal.rivenworld.game.items.weapons.sword;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.weapons.MeleeWeapon;
import com.gamefocal.rivenworld.game.recipes.weapons.SteelSwordRecipe;

public class SteelSword extends MeleeWeapon implements InventoryCraftingInterface {

    public SteelSword() {
        this.icon = InventoryDataRow.Steel_Sword;
        this.mesh = InventoryDataRow.Steel_Sword;
        this.hasDurability = true;
        this.durability = 100f;
        this.name = "Steel Short Sword";
        this.desc = "A short sword with a blade of Iron";
//        this.data.getAttributes().add("15 Damage");
        this.type = InventoryItemType.PRIMARY;
        this.tag("weapon", "oneHand");
        this.initDurability(400);
        this.spawnNames.add("steelsword");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public float hit() {
        return 15;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SteelSwordRecipe();
    }
}
