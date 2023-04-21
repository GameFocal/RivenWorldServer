package com.gamefocal.rivenworld.game.items.weapons.PickAxe;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.weapons.Pickaxe;
import com.gamefocal.rivenworld.game.recipes.weapons.StonePickaxeRecipe;

public class StonePickaxe extends Pickaxe implements InventoryCraftingInterface {

    public StonePickaxe() {
        this.isEquipable = true;
        this.type = InventoryItemType.PRIMARY;
        this.tag("weapon", "oneHand");
        this.icon = InventoryDataRow.Stone_Pickaxe;
        this.mesh = InventoryDataRow.Stone_Pickaxe;
        this.name = "Stone Pickaxe";
        this.desc = "A pickaxe made of Stone";
        this.spawnNames.add("stonepickaxe");
        this.spawnNames.add("pickaxe");
    }

    @Override
    public float hit() {
        return 2;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StonePickaxeRecipe();
    }
}
