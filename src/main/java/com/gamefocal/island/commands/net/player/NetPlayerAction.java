package com.gamefocal.island.commands.net.player;

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

@Command(name = "a-e", sources = "tcp")
public class NetPlayerAction extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        /*
         * Process the Primary Action Event
         * */
        HitResult r = netConnection.getLookingAt();

        if (FoliageHitResult.class.isAssignableFrom(r.getClass())) {

            FoliageHitResult f = (FoliageHitResult) r;

            // Foliage interact
            String hash = FoliageService.getHash(f.getName(), f.getFoliageLocation().toString());
            GameFoliageModel foliageModel = DataService.gameFoliage.queryForId(hash);

            if (foliageModel == null) {
                foliageModel = new GameFoliageModel();
                foliageModel.uuid = hash;
                foliageModel.modelName = f.getName();
                foliageModel.foliageIndex = 0;
                foliageModel.foliageState = FoliageState.GROWN;
                foliageModel.health = DedicatedServer.get(FoliageService.class).getStartingHealth(f.getName());
                foliageModel.growth = 100;
                foliageModel.location = f.getFoliageLocation();

                DataService.gameFoliage.createOrUpdate(foliageModel);

                System.out.println("New Foliage Detected...");
            }

            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.FORAGE_TREE, f.getFoliageLocation(), 5, 1f, 1f);

            List<InventoryStack> stacks = DedicatedServer.get(ForageService.class).forageFoliage(netConnection, f.getFoliageLocation(), foliageModel);

            HiveTaskSequence sequence = new HiveTaskSequence(false);
            sequence.await(20L);

            // TODO: Trigger animation on the player

            for (InventoryStack s : stacks) {
                netConnection.getPlayer().inventory.add(s);
                sequence.exec(() -> {
                    netConnection.displayItemAdded(s);
                });
            }
            sequence.await(5L);
            sequence.exec(() -> {
                netConnection.updateInventory(netConnection.getPlayer().inventory);
            });

            TaskService.scheduleTaskSequence(sequence);

        } else if (EntityHitResult.class.isAssignableFrom(r.getClass())) {
            // Entity Interact

            GameEntity e = (GameEntity) r.get();
            if (InteractableEntity.class.isAssignableFrom(e.getClass())) {
                if (((InteractableEntity) e).canInteract(netConnection)) {

                    // Get Inhand
                    InventoryStack inhand = netConnection.getPlayer().equipmentSlots.getWeapon();

                    ((InteractableEntity) e).onInteract(netConnection, InteractAction.USE, inhand);
                }
            }

        } else if (TerrainHitResult.class.isAssignableFrom(r.getClass())) {
            // Forage from the ground.

            TerrainHitResult t = (TerrainHitResult) r;

            GameSounds sfx = null;

            if (t.getTypeOfGround().equalsIgnoreCase("Rocks")) {
                sfx = GameSounds.FORAGE_ROCK;
            } else if (t.getTypeOfGround().equalsIgnoreCase("Dirt")) {
                sfx = GameSounds.FORAGE_DIRT;
            } else if (t.getTypeOfGround().equalsIgnoreCase("Grass")) {
                sfx = GameSounds.FORAGE_GRASS;
            } else if (t.getTypeOfGround().equalsIgnoreCase("Sand")) {
                sfx = GameSounds.FORAGE_SAND;
            }

            if (sfx != null) {
                DedicatedServer.instance.getWorld().playSoundAtLocation(sfx, t.getLocation(), 5, 1f, 1f);
            }

            List<InventoryStack> stacks = DedicatedServer.get(ForageService.class).forageGround(netConnection, t.getTypeOfGround(), t.getLocation());

            HiveTaskSequence sequence = new HiveTaskSequence(false);
            sequence.await(20L);

            // TODO: Trigger animation on the player

            for (InventoryStack s : stacks) {
                netConnection.getPlayer().inventory.add(s);
                sequence.exec(() -> {
                    netConnection.displayItemAdded(s);
                });
            }
            sequence.await(5L);
            sequence.exec(() -> {
                netConnection.updateInventory(netConnection.getPlayer().inventory);
            });

            TaskService.scheduleTaskSequence(sequence);
        }


    }
}
