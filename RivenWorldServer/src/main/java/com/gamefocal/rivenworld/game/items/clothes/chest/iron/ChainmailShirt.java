package com.gamefocal.rivenworld.game.items.clothes.chest.iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.clothes.generics.PlayerChestItem;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.ChainmailIronShirt_R;

public class ChainmailShirt extends PlayerChestItem {

    public ChainmailShirt() {
        this.name = "Chainmail Shirt";
        this.desc = "A shirt made of chainmail";
        this.icon = InventoryDataRow.Chest1_Heavy_T4;
        this.mesh = InventoryDataRow.Chest1_Heavy_T4;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ChainmailIronShirt_R();
    }

    @Override
    public float defend() {
        return 1;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "ChainmailShirt";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
