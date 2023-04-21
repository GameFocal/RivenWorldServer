package com.gamefocal.rivenworld.game.items.weapons.PickAxe;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;
import com.gamefocal.rivenworld.game.recipes.weapons.IronPickaxeRecipe;

public class IronPickaxe extends Pickaxe implements InventoryCraftingInterface {

    public IronPickaxe() {
        this.isEquipable = true;
        this.type = InventoryItemType.PRIMARY;
        this.tag("weapon", "oneHand");
        this.icon = InventoryDataRow.Iron_Pickaxe;
        this.mesh = InventoryDataRow.Iron_Pickaxe;
        this.name = "Iron Pickaxe";
        this.desc = "A pickaxe made of Iron";
        this.initDurability(200);
        this.spawnNames.add("ironpickaxe");
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
        return new IronPickaxeRecipe();
    }
}
