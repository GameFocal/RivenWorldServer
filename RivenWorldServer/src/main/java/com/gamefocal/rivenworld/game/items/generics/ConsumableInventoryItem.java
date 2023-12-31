package com.gamefocal.rivenworld.game.items.generics;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.service.TaskService;

public abstract class ConsumableInventoryItem extends InventoryItem implements UsableInventoryItem, EquipmentItem {

    protected boolean useThirstModifier = false;
    protected float thirstModifier = 0;

    public ConsumableInventoryItem() {
        this.type = InventoryItemType.CONSUMABLE;
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;

        this.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "Food: +" + this.onConsume(null));
        if (this.useThirstModifier) {
            this.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "Thirst: " + this.thirstModifier);
        } else {
            this.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "Thirst: +" + Math.max(1, this.onConsume(null) / 2));
        }
    }

    public abstract float onConsume(HiveNetConnection connection);

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
        // Do Nothing
    }

    @Override
    public void generateUpperRightHelpText() {
        this.upperRightText.add("[E] Consume");
    }

    @Override
    public String inHandTip(HiveNetConnection connection, HitResult hitResult) {
        return "[e] Consume";
    }

    @Override
    public boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand) {
        if (inHand.getAmount() > 0) {

            TaskService.schedulePlayerInterruptTask(() -> {
                connection.playAnimation(Animation.Eat, "RightArmSlot", 1, .25f, -1, 1, 0.25f, true);
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.EAT, connection.getPlayer().location, 150f, 1f, 1f, 1);

                connection.getPlayer().playerStats.hunger += this.onConsume(connection);
                connection.getPlayer().playerStats.thirst += (this.useThirstModifier ? this.thirstModifier : (Math.max(1, this.onConsume(connection) / 2)));
                inHand.remove(1);
                connection.getPlayer().inventory.update();
                connection.updatePlayerInventory();
                connection.syncEquipmentSlots();
            }, 3L, "Eating", Color.GREEN, connection);

//            connection.playAnimation(Animation.Eat);
            return true;
        }

        return false;
    }

    @Override
    public String toSocket() {
        return "Primary";
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }
}
