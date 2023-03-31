package com.gamefocal.rivenworld.game.items.weapons.sword;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.weapons.MeleeWeapon;
import com.gamefocal.rivenworld.game.recipes.weapons.SteelLongSwordRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.SteelSwordRecipe;

public class Steel_LongSword extends MeleeWeapon implements InventoryCraftingInterface {

    public Steel_LongSword() {
        this.icon = InventoryDataRow.Steel_Longsword;
        this.mesh = InventoryDataRow.Steel_Longsword;
        this.hasDurability = true;
        this.durability = 100f;
        this.name = "Steel Long Sword";
        this.desc = "A long sword with a blade of Steel";
        this.data.getAttributes().add("15 Damage");
        this.type = InventoryItemType.PRIMARY;
        this.tag("weapon", "twoHand");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public float hit() {
        return 35;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SteelLongSwordRecipe();
    }
}