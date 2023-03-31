package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.recipes.weapons.TorchRecipe;

public class Torch extends ToolInventoryItem implements InventoryCraftingInterface, UsableInventoryItem {

    public Torch() {
        this.icon = InventoryDataRow.Torch;
        this.mesh = InventoryDataRow.Torch;
        this.name = "Torch";
        this.desc = "Used to provide light around your character";
        this.spawnNames.add("torch");
        this.initDurability(200);
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
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new TorchRecipe();
    }

    @Override
    public String inHandTip(HiveNetConnection connection, HitResult hitResult) {
        return "[e] Light Torch";
    }

    @Override
    public boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand) {
        if (this.hasTag("on")) {
            if (this.tagEquals("on", "t")) {
                this.tag("on", "f");
            } else {
                this.tag("on", "t");
            }
        } else {
            this.tag("on", "t");
        }

        connection.updatePlayerInventory();
        connection.syncEquipmentSlots();

        connection.playAnimation(Animation.Torch);

        return true;
    }
}
