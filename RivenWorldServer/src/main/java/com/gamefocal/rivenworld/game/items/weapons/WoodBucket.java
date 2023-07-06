package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.generics.EquipmentItem;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.items.resources.water.CleanWaterBucket;
import com.gamefocal.rivenworld.game.items.resources.water.DirtyWaterBucket;
import com.gamefocal.rivenworld.game.items.resources.water.SaltWaterBucket;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.WaterHitResult;
import com.gamefocal.rivenworld.game.recipes.WoodBucketRecipe;
import com.gamefocal.rivenworld.game.water.WaterSource;

public class WoodBucket extends InventoryItem implements InventoryCraftingInterface, UsableInventoryItem, EquipmentItem {

    public WoodBucket() {
        this.icon = InventoryDataRow.Wooden_Bucket;
        this.mesh = InventoryDataRow.Wooden_Bucket;
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
        this.name = "Wooden bucket";
        this.desc = "An empty bucket made of wood";
        this.spawnNames.add("bucket");
        this.isStackable = false;
        this.hasDurability = true;
        this.durability = 100;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodBucketRecipe();
    }

    @Override
    public void generateUpperRightHelpText() {
        this.upperRightText.add("[E] Gather water");
    }

    @Override
    public String inHandTip(HiveNetConnection connection, HitResult hitResult) {
        if(hitResult != null) {
            if (WaterHitResult.class.isAssignableFrom(hitResult.getClass())) {
                return "[e] Gather water";
            }
        }
        return "Go to the nearest water source to collect water.";
    }

    @Override
    public boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand) {
        WaterHitResult waterHitResult = (WaterHitResult) hitResult;
        if (waterHitResult != null) {
//            connection.playAnimation(Animation.GATHER_WATER);
            connection.playAnimation(Animation.GATHER_WATER, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, true);

            InventoryItem newItem = null;

            if (waterHitResult.get() == WaterSource.FRESH_WATER) {
//                inHand.setAmount(inHand.getAmount() - 1);
//                connection.getPlayer().inventory.add(new DirtyWaterBucket(), 1);
                newItem = new DirtyWaterBucket();
            } else if (waterHitResult.get() == WaterSource.SALT_WATER) {
//                inHand.setAmount(inHand.getAmount() - 1);
//                connection.getPlayer().inventory.add(new SaltWaterBucket(), 1);
                newItem = new DirtyWaterBucket();
            }

            if(newItem != null) {
                newItem.setDurability(this.durability);
            }

            connection.getPlayer().equipmentSlots.inHand.setItem(newItem);
            connection.getPlayer().equipmentSlots.inHand.setAmount(1);
        }
        connection.updatePlayerInventory();
        connection.syncEquipmentSlots();
        return true;
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "Primary";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
