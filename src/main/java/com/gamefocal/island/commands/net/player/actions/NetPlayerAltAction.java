package com.gamefocal.island.commands.net.player.actions;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.foliage.FoliageState;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.ray.HitResult;
import com.gamefocal.island.game.ray.hit.EntityHitResult;
import com.gamefocal.island.game.ray.hit.FoliageHitResult;
import com.gamefocal.island.game.ray.hit.TerrainHitResult;
import com.gamefocal.island.game.sounds.GameSounds;
import com.gamefocal.island.game.tasks.HiveTaskSequence;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.FoliageService;
import com.gamefocal.island.service.ForageService;
import com.gamefocal.island.service.TaskService;

import java.util.List;

@Command(name = "a-q", sources = "tcp")
public class NetPlayerAltAction extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println("ALT");

        /*
         * Process the Primary Action Event
         * */
        HitResult r = netConnection.getLookingAt();

        if (EntityHitResult.class.isAssignableFrom(r.getClass())) {
            // Entity Interact

            GameEntity e = (GameEntity) r.get();
            if (InteractableEntity.class.isAssignableFrom(e.getClass())) {
                if (((InteractableEntity) e).canInteract(netConnection)) {

                    // Get Inhand
                    InventoryStack inhand = netConnection.getPlayer().equipmentSlots.getWeapon();

                    ((InteractableEntity) e).onInteract(netConnection, InteractAction.ALT, inhand);
                }
            }

        }


    }
}
