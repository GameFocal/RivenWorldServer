package com.gamefocal.rivenworld.game.items.weapons.hatchets;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.recipes.weapons.StoneHatchetRecipe;

public class StoneHatchet extends Hatchet implements InventoryCraftingInterface {

    public StoneHatchet() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
        this.tag("weapon", "oneHand");
        this.type = InventoryItemType.PRIMARY;
        this.icon = InventoryDataRow.Stone_Hatchet;
        this.mesh = InventoryDataRow.Stone_Hatchet;
        this.name = "Stone Axe";
        this.desc = "A axe made of stone";
        this.spawnNames.add("stoneaxe");
    }

    @Override
    public float hit() {
        return 4;
    }

    @Override
    public float block() {
        return 10;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneHatchetRecipe();
    }
}
