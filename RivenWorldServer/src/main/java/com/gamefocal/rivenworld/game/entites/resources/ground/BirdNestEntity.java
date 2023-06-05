package com.gamefocal.rivenworld.game.entites.resources.ground;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.misc.Feather;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.skills.skillTypes.ForagingSkill;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.service.ResourceService;
import com.gamefocal.rivenworld.service.SkillService;
import com.gamefocal.rivenworld.service.TaskService;

public class BirdNestEntity extends GameEntity<BirdNestEntity> implements InteractableEntity {

    public BirdNestEntity() {
        this.type = "BirdNest";
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.USE) {
//            DedicatedServer.get(ResourceService.class)

//            InventoryStack stack = new InventoryStack(new Fiber(), 5);
//
//            connection.getPlayer().inventory.add(stack);
//            connection.displayItemAdded(stack);

//            connection.setAnimationCallback((connection1, args) -> {
//                InventoryStack stack = new InventoryStack(new Feather(), RandomUtil.getRandomNumberBetween(1,10));
//                connection.getPlayer().inventory.add(stack);
//                connection.displayItemAdded(stack);
//                SkillService.addExp(connection, ForagingSkill.class, 2);
//                DedicatedServer.get(ResourceService.class).oneOffNodeHarvest(this);
//                connection.updatePlayerInventory();
//                connection.syncEquipmentSlots();
//            });

            TaskService.schedulePlayerInterruptTask(() -> {
                InventoryStack stack = new InventoryStack(new Feather(), RandomUtil.getRandomNumberBetween(1,10));
                connection.getPlayer().inventory.add(stack);
                connection.displayItemAdded(stack);
                SkillService.addExp(connection, ForagingSkill.class, 2);
                DedicatedServer.get(ResourceService.class).oneOffNodeHarvest(this);
                connection.updatePlayerInventory();
                connection.syncEquipmentSlots();
            }, 5L, "Gathering Fiber", Color.GRAY, connection);

//            connection.playAnimation(Animation.FORAGE_GROUND);
            connection.playAnimation(Animation.FORAGE_GROUND, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, true);
            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.FORAGE_GRASS, this.location, 200f, 1, 1);
        }
    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Search Bird Nest";
    }
}
