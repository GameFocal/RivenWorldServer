package com.gamefocal.rivenworld.game.items.weapons.hatchets;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.recipes.weapons.IronHatchetRecipe;

public class IronHatchet extends Hatchet implements InventoryCraftingInterface {

    public IronHatchet() {
        this.isEquipable = true;
        this.type = InventoryItemType.PRIMARY;
        this.tag("weapon", "oneHand");
        this.icon = InventoryDataRow.Iron_Hatchet;
        this.mesh = InventoryDataRow.Iron_Hatchet;
        this.name = "Iron Axe";
        this.desc = "A axe made of iron";
        this.initDurability(200);
        this.spawnNames.add("ironaxe");
    }

    @Override
    public float hit() {
        return 6;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new IronHatchetRecipe();
    }
}
