package com.gamefocal.rivenworld.game.items.resources.water;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.EquipmentItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.WoodBucket;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.service.TaskService;

public class DirtyWaterBucket extends InventoryItem implements UsableInventoryItem, EquipmentItem {

    public DirtyWaterBucket() {
        this.icon = InventoryDataRow.Wooden_Bucket;
        this.mesh = InventoryDataRow.Wooden_Bucket;
        this.name = "Bucket with Dirty Water";
        this.desc = "A bucket filled with Dirty Water, be careful not to drink or you may get sick";
        this.tint = Color.BROWN;
        this.spawnNames.add("dirtywater");
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

            connection.getPlayer().playerStats.thirst += 20;
            inHand.setItem(emptyBucket);

//            connection.playAnimation(Animation.Eat);
            connection.playAnimation(Animation.Eat, "RightArmSlot", 1, .25f, -1, 1, 0.25f, true);
            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.EAT, connection.getPlayer().location, 150f, 1f, .15f);

            inHand.getItem().setDurability(inHand.getItem().getDurability() - 10);
            if(inHand.getItem().getDurability() <= 0) {
                // Break
                connection.breakItemInSlot(EquipmentSlot.PRIMARY);
            }

            connection.updatePlayerInventory();
            connection.syncEquipmentSlots();

            if (RandomUtil.getRandomChance(.35)) {
                // Chance to get sick
                TaskService.scheduleRepeatingLimitedTask(() -> {
                    connection.getPlayer().playerStats.health -= 1f;
                }, 20L, 20L, 30, false);

                connection.sendChatMessage(ChatColor.ORANGE + "You feel sick");
            }

            return true;
        }

        return false;
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
