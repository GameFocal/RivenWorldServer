package com.gamefocal.rivenworld.game.items.weapons.hatchets;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.recipes.weapons.StoneHatchetRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.WoodHatchetRecipe;

public class WoodHatchet extends Hatchet implements InventoryCraftingInterface {

    public WoodHatchet() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
        this.tag("weapon", "oneHand");
        this.type = InventoryItemType.PRIMARY;
        this.icon = InventoryDataRow.Wood_Hatchet;
        this.mesh = InventoryDataRow.Wood_Hatchet;
        this.name = "Wood Axe";
        this.desc = "A axe made of Wood";
        this.spawnNames.add("woodeaxe");
        this.spawnNames.add("axe");
        this.initDurability(50);
    }

    @Override
    public float hit() {
        return 1;
    }

    @Override
    public float block() {
        return 5;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodHatchetRecipe();
    }
}
