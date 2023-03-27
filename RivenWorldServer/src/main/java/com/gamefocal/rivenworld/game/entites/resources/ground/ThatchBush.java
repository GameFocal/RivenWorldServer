package com.gamefocal.rivenworld.game.entites.resources.ground;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.skills.skillTypes.ForagingSkill;
import com.gamefocal.rivenworld.game.skills.skillTypes.WoodcuttingSkill;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.service.ResourceService;
import com.gamefocal.rivenworld.service.SkillService;

public class ThatchBush extends GameEntity<ThatchBush> implements InteractableEntity {

    public ThatchBush() {
        this.type = "thatch-bush";
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

            InventoryStack stack = new InventoryStack(new Fiber(), 5);

            connection.getPlayer().inventory.add(stack);
            connection.displayItemAdded(stack);
            connection.playAnimation(Animation.FORAGE_GROUND);
            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.FORAGE_GRASS, this.location, 200f, 1, 1);

            SkillService.addExp(connection, ForagingSkill.class, 2);

            DedicatedServer.get(ResourceService.class).oneOffNodeHarvest(this);
        }
    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Harvest Fiber";
    }
}
