package com.gamefocal.rivenworld.commands.net.player.actions;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.player.PlayerInteractEvent;
import com.gamefocal.rivenworld.events.resources.PlayerForageEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;
import com.gamefocal.rivenworld.game.ray.hit.FoliageHitResult;
import com.gamefocal.rivenworld.game.ray.hit.TerrainHitResult;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.FoliageService;
import com.gamefocal.rivenworld.service.ForageService;
import com.gamefocal.rivenworld.service.InventoryService;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Command(name = "a-e", sources = "tcp")
public class NetPlayerAction extends HiveCommand {

    public static ConcurrentHashMap<UUID, Long> interactTimer = new ConcurrentHashMap<>();

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        if (netConnection.getAnimationCallback() != null || netConnection.getPlayerInteruptTask() != null) {
            return;
        }

        new PlayerInteractEvent(netConnection).call();

        try {
            /*
             * Process the Primary Action Event
             * */
            HitResult r = netConnection.getLookingAt();

            if (r != null) {

                if (EntityHitResult.class.isAssignableFrom(r.getClass())) {
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
                            return;
                        }
                    }

                }

                if (FoliageHitResult.class.isAssignableFrom(r.getClass())) {

                    FoliageHitResult f = (FoliageHitResult) r;

                    // Foliage interact
                    String hash = FoliageService.getHash(f.getName(), f.getFoliageLocation().toString());
                    DedicatedServer.get(FoliageService.class).registerNewFoliage(f);
                    GameFoliageModel foliageModel = DedicatedServer.get(FoliageService.class).getFoliage(hash);

                    TaskService.schedulePlayerInterruptTask(() -> {
                        List<InventoryStack> stacks = DedicatedServer.get(ForageService.class).forageFoliage(netConnection, f.getFoliageLocation(), foliageModel);

                        if (stacks.size() > 0) {
                            new PlayerForageEvent(netConnection, r).call();
                        }

                        for (InventoryStack s : stacks) {
                            if (netConnection.getPlayer().inventory.canAdd(s)) {
                                netConnection.getPlayer().inventory.add(s);
                                netConnection.displayItemAdded(s);
                            } else {
                                netConnection.displayInventoryFull();
                            }
                        }

                        netConnection.updateInventory(netConnection.getPlayer().inventory);
                        netConnection.updatePlayerInventory();
//                        netConnection.enableMovment();
                    }, 5L, "Foraging", Color.GRAY, netConnection);

//                    netConnection.disableMovment();
                    netConnection.playAnimation(Animation.FORAGE_TREE, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, true);
                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.FORAGE_TREE, f.getFoliageLocation(), 5, 1f, 1f);

                    return;
                }

                if (TerrainHitResult.class.isAssignableFrom(r.getClass())) {
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

                    GameSounds finalSfx = sfx;
//                    netConnection.setAnimationCallback((connection, args) -> {
//                    });

                    TaskService.schedulePlayerInterruptTask(() -> {
                        List<InventoryStack> stacks = DedicatedServer.get(ForageService.class).forageGround(netConnection, t.getTypeOfGround(), t.getLocation());

                        if (stacks.size() > 0) {
                            new PlayerForageEvent(netConnection, r).call();
                        }

                        for (InventoryStack s : stacks) {
                            if (netConnection.getPlayer().inventory.canAdd(s)) {
                                netConnection.getPlayer().inventory.add(s);
                                netConnection.displayItemAdded(s);
                            } else {
                                netConnection.displayInventoryFull();
                            }
                        }

                        netConnection.updateInventory(netConnection.getPlayer().inventory);
                        netConnection.updatePlayerInventory();
//                        netConnection.enableMovment();
                    }, 5L, "Foraging", Color.GRAY, netConnection);

//                    netConnection.disableMovment();
                    netConnection.playAnimation(Animation.FORAGE_GROUND, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, true);
                    if (finalSfx != null) {
                        DedicatedServer.instance.getWorld().playSoundAtLocation(finalSfx, t.getLocation(), 5, 1f, 1f);
                    }

                    return;
                }
            }

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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
