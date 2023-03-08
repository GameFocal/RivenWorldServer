package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.items.resources.water.SaltWaterBucket;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.WaterHitResult;
import com.gamefocal.rivenworld.game.recipes.WoodBucketRecipe;

public class WoodBucket extends ToolInventoryItem implements InventoryCraftingInterface, UsableInventoryItem {

    public WoodBucket() {
        this.icon = InventoryDataRow.Wooden_Bucket;
        this.mesh = InventoryDataRow.Wooden_Bucket;
        this.name = "Wooden bucket.";
        this.desc = "An empty bucket made of wood.";
        this.spawnNames.add("bucket");
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
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodBucketRecipe();
    }

    @Override
    public String inHandTip(HiveNetConnection connection, HitResult hitResult) {
            return "[e] Gather Water";
    }

    @Override
    public boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand) {
        connection.playAnimation(Animation.GATHER_WATER);
        inHand.setAmount(0);
        connection.getPlayer().inventory.add(new SaltWaterBucket(),1);
        connection.updatePlayerInventory();
        connection.syncEquipmentSlots();
        return true;
    }
}
