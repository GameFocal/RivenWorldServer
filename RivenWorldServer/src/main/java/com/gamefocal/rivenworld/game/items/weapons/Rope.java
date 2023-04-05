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
import com.gamefocal.rivenworld.game.items.generics.EquipmentItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.items.resources.water.DirtyWaterBucket;
import com.gamefocal.rivenworld.game.items.resources.water.SaltWaterBucket;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.PlayerHitResult;
import com.gamefocal.rivenworld.game.ray.hit.WaterHitResult;
import com.gamefocal.rivenworld.game.recipes.RopeRecipe;
import com.gamefocal.rivenworld.game.recipes.WoodBucketRecipe;
import com.gamefocal.rivenworld.game.water.WaterSource;

public class Rope extends InventoryItem implements InventoryCraftingInterface, UsableInventoryItem, EquipmentItem {

    public Rope() {
        this.icon = InventoryDataRow.Rope_Around_Beam_01;
        this.mesh = InventoryDataRow.Rope_Around_Beam_01;
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
        this.name = "Rope";
        this.desc = "An Item to capture your enemies";
        this.spawnNames.add("rope");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new RopeRecipe();
    }

    @Override
    public String inHandTip(HiveNetConnection connection, HitResult hitResult) {
        if(hitResult != null) {
            if (PlayerHitResult.class.isAssignableFrom(hitResult.getClass())) {
                return "[e] Capture";
            }
        }
        return "Get close to a player to capture.";
    }

    @Override
    public boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand) {

        if(PlayerHitResult.class.isAssignableFrom(hitResult.getClass())) {
            PlayerHitResult playerHitResult = (PlayerHitResult) hitResult;
//            connection.dragPlayer(playerHitResult.get());
            connection.CapturePlayer(playerHitResult.get());
        }

//        connection.playAnimation(Animation.GATHER_WATER);
//        inHand.setAmount(inHand.getAmount() - 1);
//        WaterHitResult waterHitResult = (WaterHitResult) hitResult;
//        if(waterHitResult.getSource() == WaterSource.FRESH_WATER){
//            connection.getPlayer().inventory.add(new DirtyWaterBucket(),1);
//        } else if (waterHitResult.getSource() == WaterSource.SALT_WATER){
//            connection.getPlayer().inventory.add(new SaltWaterBucket(), 1);
//        }
//        connection.updatePlayerInventory();
//        connection.syncEquipmentSlots();
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
