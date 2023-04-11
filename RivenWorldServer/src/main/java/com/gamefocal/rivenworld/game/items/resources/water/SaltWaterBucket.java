package com.gamefocal.rivenworld.game.items.resources.water;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.EquipmentItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.service.TaskService;

public class SaltWaterBucket extends InventoryItem implements UsableInventoryItem, EquipmentItem {

    public SaltWaterBucket() {
        this.icon = InventoryDataRow.Wooden_Bucket;
        this.mesh = InventoryDataRow.Wooden_Bucket;
        this.name = "Bucket with Salt Water";
        this.desc = "A bucket filled with Salt Water, be careful not to drink or you may get sick";
        this.tint = Color.YELLOW;
        this.spawnNames.add("saltwater");
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
        this.isStackable = false;
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
            connection.getPlayer().playerStats.thirst += 25;
            inHand.remove(1);
            connection.getPlayer().inventory.update();
            connection.updatePlayerInventory();

            connection.playAnimation(Animation.Eat);
            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.EAT, connection.getPlayer().location, 150f, 1f, .15f);

            if (RandomUtil.getRandomChance(.25)) {
                // Chance to get sick
                TaskService.scheduleRepeatingLimitedTask(() -> {
                    connection.getPlayer().playerStats.thirst -= 2f;
                }, 20L, 20L, 30, false);
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
