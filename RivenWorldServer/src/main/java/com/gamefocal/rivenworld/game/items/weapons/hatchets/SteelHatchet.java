package com.gamefocal.rivenworld.game.items.weapons.hatchets;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.recipes.weapons.SteelHatchetRecipe;

public class SteelHatchet extends Hatchet implements InventoryCraftingInterface {

    public SteelHatchet() {
        this.isEquipable = true;
        this.type = InventoryItemType.PRIMARY;
        this.tag("weapon", "oneHand");
        this.icon = InventoryDataRow.Steel_Hatchet;
        this.mesh = InventoryDataRow.Steel_Hatchet;
        this.name = "Steel Axe";
        this.desc = "A axe made of steel";
        this.initDurability(400);
        this.spawnNames.add("steelaxe");
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
        return new SteelHatchetRecipe();
    }
}
