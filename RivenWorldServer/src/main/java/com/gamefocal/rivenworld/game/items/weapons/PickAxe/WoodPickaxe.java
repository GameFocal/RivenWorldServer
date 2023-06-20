package com.gamefocal.rivenworld.game.items.weapons.PickAxe;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;
import com.gamefocal.rivenworld.game.recipes.weapons.StonePickaxeRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.WoodPickaxeRecipe;

public class WoodPickaxe extends Pickaxe implements InventoryCraftingInterface {

    public WoodPickaxe() {
        this.isEquipable = true;
        this.type = InventoryItemType.PRIMARY;
        this.tag("weapon", "oneHand");
        this.icon = InventoryDataRow.Wood_Pickaxe;
        this.mesh = InventoryDataRow.Wood_Pickaxe;
        this.name = "Wood Pickaxe";
        this.desc = "A pickaxe made of Wood";
        this.spawnNames.add("woodpickaxe");
        this.spawnNames.add("pickaxe");
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
        return new WoodPickaxeRecipe();
    }
}
