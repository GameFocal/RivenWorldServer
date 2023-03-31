package com.gamefocal.rivenworld.commands.net.player.actions;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.player.PlayerInteractEvent;
import com.gamefocal.rivenworld.events.resources.PlayerForageEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.foliage.FoliageState;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;
import com.gamefocal.rivenworld.game.ray.hit.FoliageHitResult;
import com.gamefocal.rivenworld.game.ray.hit.TerrainHitResult;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.tasks.HiveTaskSequence;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.FoliageService;
import com.gamefocal.rivenworld.service.ForageService;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Command(name = "a-e", sources = "tcp")
public class NetPlayerAction extends HiveCommand {

    public static ConcurrentHashMap<UUID, Long> interactTimer = new ConcurrentHashMap<>();

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        if (interactTimer.containsKey(netConnection.getUuid())) {
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - interactTimer.get(netConnection.getUuid())) <= 1) {
                return;
            }
        }

        interactTimer.put(netConnection.getUuid(), System.currentTimeMillis());

        new PlayerInteractEvent(netConnection).call();

        /*
         * Process in-hand call
         * */
        if (netConnection.getPlayer().equipmentSlots.inHand != null) {
            if (UsableInventoryItem.class.isAssignableFrom(netConnection.getPlayer().equipmentSlots.inHand.getItem().getClass())) {
                // Is a usable item
                UsableInventoryItem ui = (UsableInventoryItem) netConnection.getPlayer().equipmentSlots.inHand.getItem();
                if (ui.onUse(netConnection, netConnection.getLookingAt(), InteractAction.USE, netConnection.getPlayer().equipmentSlots.inHand)) {
                    return;
                }
            }
        }

        try {
            /*
             * Process the Primary Action Event
             * */
            HitResult r = netConnection.getLookingAt();

            if (r == null) {
                return;
            }

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

                if (stacks.size() > 0) {
                    new PlayerForageEvent(netConnection, r).call();
                }

                HiveTaskSequence sequence = new HiveTaskSequence(false);
                sequence.await(20L);

                // TODO: Trigger animation on the player
                netConnection.playAnimation(Animation.FORAGE_TREE);

                for (InventoryStack s : stacks) {
                    netConnection.getPlayer().inventory.add(s);
                    sequence.exec(() -> {
                        netConnection.displayItemAdded(s);
                    });
                }
                sequence.await(5L);
                sequence.exec(() -> {
                    netConnection.updateInventory(netConnection.getPlayer().inventory);
                    netConnection.updatePlayerInventory();
                });

                TaskService.scheduleTaskSequence(sequence);

            } else if (EntityHitResult.class.isAssignableFrom(r.getClass())) {
                // Entity Interact

                GameEntity e = (GameEntity) r.get();
                if (InteractableEntity.class.isAssignableFrom(e.getClass())) {
                    if (((InteractableEntity) e).canInteract(netConnection)) {

                        // Check for interact perms
                        WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(e.location);
                        if (chunk != null) {
                            if (!chunk.canInteract(netConnection)) {
                                return;
                            }
                        }

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

                if (stacks.size() > 0) {
                    new PlayerForageEvent(netConnection, r).call();
                }

                HiveTaskSequence sequence = new HiveTaskSequence(false);
                sequence.await(20L);

                // TODO: Trigger animation on the player
                netConnection.playAnimation(Animation.FORAGE_GROUND);

                for (InventoryStack s : stacks) {
                    netConnection.getPlayer().inventory.add(s);
                    sequence.exec(() -> {
                        netConnection.displayItemAdded(s);
                    });
                }
                sequence.await(5L);
                sequence.exec(() -> {
                    netConnection.updateInventory(netConnection.getPlayer().inventory);
                    netConnection.updatePlayerInventory();
                });

                TaskService.scheduleTaskSequence(sequence);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
