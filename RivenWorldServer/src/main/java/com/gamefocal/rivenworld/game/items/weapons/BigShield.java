package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.recipes.weapons.BigShieldRecipe;

public class BigShield extends ToolInventoryItem implements InventoryCraftingInterface {

    public BigShield() {
        this.icon = InventoryDataRow.Big_Shield;
        this.mesh = InventoryDataRow.Big_Shield;
        this.equipTo = EquipmentSlot.BACK;
        this.type = InventoryItemType.SECONDARY;
        this.name = "Large Iron Shield";
        this.desc = "A large shield forged from Iron";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public float hit() {
        return 0;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public String toSocket() {
        return "Back";
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new BigShieldRecipe();
    }
}
