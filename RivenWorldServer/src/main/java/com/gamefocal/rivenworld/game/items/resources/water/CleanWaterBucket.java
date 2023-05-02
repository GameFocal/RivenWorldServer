package com.gamefocal.rivenworld.game.items.resources.water;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
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
import com.gamefocal.rivenworld.game.items.weapons.WoodBucket;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.recipes.resources.CleanWaterFromDirtyRecipe;
import com.gamefocal.rivenworld.game.sounds.GameSounds;

public class CleanWaterBucket extends InventoryItem implements UsableInventoryItem, InventoryCraftingInterface, EquipmentItem {

    public CleanWaterBucket() {
        this.icon = InventoryDataRow.Wooden_Bucket;
        this.mesh = InventoryDataRow.Wooden_Bucket;
        this.name = "Bucket with Clean Water";
        this.desc = "A bucket filled with Clean Water that you can use for drinking or cooking";
        this.tint = Color.CYAN;
        this.spawnNames.add("water");
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
        this.isStackable = false;
        this.hasDurability = true;
        this.durability = 100;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public String inHandTip(HiveNetConnection connection, HitResult hitResult) {
        return "[e] To Drink";
    }

    @Override
    public boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand) {
        if (inHand.getAmount() > 0) {

            WoodBucket emptyBucket = new WoodBucket();
            emptyBucket.setDurability(this.durability);

            connection.getPlayer().playerStats.thirst += 25;
            inHand.setItem(emptyBucket);
            connection.getPlayer().inventory.update();

//            connection.playAnimation(Animation.Eat);
            connection.playAnimation(Animation.Eat, "UpperBody", 1, 0, -1, true);
            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.EAT, connection.getPlayer().location, 150f, 1f, .15f);

            inHand.getItem().setDurability(inHand.getItem().getDurability() - 10);
            if(inHand.getItem().getDurability() <= 0) {
                // Break
                connection.breakItemInSlot(EquipmentSlot.PRIMARY);
            }

            connection.updatePlayerInventory();
            connection.syncEquipmentSlots();

            return true;
        }

        return false;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CleanWaterFromDirtyRecipe();
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
