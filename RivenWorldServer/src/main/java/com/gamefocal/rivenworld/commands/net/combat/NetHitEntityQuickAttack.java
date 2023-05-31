package com.gamefocal.rivenworld.commands.net.combat;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.player.Montage;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;
import com.gamefocal.rivenworld.game.ray.hit.FoliageHitResult;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.tasks.seqence.ExecSequenceAction;
import com.gamefocal.rivenworld.game.tasks.seqence.WaitSequenceAction;
import com.gamefocal.rivenworld.service.CombatService;
import com.gamefocal.rivenworld.service.FoliageService;
import com.gamefocal.rivenworld.service.ResourceService;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Command(name = "nchq", sources = "tcp")
public class NetHitEntityQuickAttack extends HiveCommand {

    public static ConcurrentHashMap<UUID, Long> lastHit = new ConcurrentHashMap<>();

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.getAnimationCallback() != null || netConnection.getPlayerInteruptTask() != null) {
            return;
        }

        InventoryStack inHand = netConnection.getPlayer().equipmentSlots.inHand;
        HitResult hitResult = netConnection.getLookingAt();

        if (inHand == null) {

            /*
             * Nothing in-hand, PUNCH
             * */

            // No weapon or tool in Hand.
            netConnection.setAnimationCallback((connection, args) -> {
                // Nothing
//                netConnection.enableMovment();
            });
//            netConnection.disableMovment();
            netConnection.playAnimation(Animation.PUNCH, "UpperBody", 1, 0, -1, 0.25f, 0.25f, true);
            DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.FORWARD, 100, true);
        }

        if (hitResult != null && FoliageHitResult.class.isAssignableFrom(hitResult.getClass())) {

            /*
             * Hit Result is a tree, harvest the tree
             * */

            FoliageHitResult foliageHitResult = (FoliageHitResult) hitResult;
            DedicatedServer.get(FoliageService.class).harvest(foliageHitResult, netConnection);
            return;

        } else if (hitResult != null && EntityHitResult.class.isAssignableFrom(hitResult.getClass())) {

            /*
             * Hit Result is an entity
             * */

            if (LivingEntity.class.isAssignableFrom(hitResult.get().getClass()) && !((LivingEntity) hitResult.get()).isAlive()) {


                /*
                 * Dead living entity, used for harvesting meat and hide from the animal
                 * */

                LivingEntity livingEntity = (LivingEntity) hitResult.get();

                if (!livingEntity.isAlive()) {
                    // Is dead
                    if (livingEntity.canBeDamaged) {

                        netConnection.disableInteraction(1500);

                        if (livingEntity.onHarvest(netConnection)) {

                            /*
                             * Animation callback for harvesting animal remains
                             * */
                            netConnection.setAnimationCallback((connection, args) -> {
//                                connection.enableMovment();
                                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.HIT_FLESH, livingEntity.location, 1500, 1f, 1f);
                                livingEntity.health -= 5;
                                netConnection.showFloatingTxt("-5", livingEntity.location);
                                netConnection.flashProgressBar(livingEntity.getClass().getSimpleName(), livingEntity.health / livingEntity.maxHealth, Color.ORANGE, 5);

                                if (livingEntity.health <= 0) {
                                    // Despawn the animal
                                    DedicatedServer.instance.getWorld().despawn(livingEntity.uuid);
                                    return;
                                }
                            });
//                            netConnection.disableMovment();
                            netConnection.playAnimation(Animation.PICKAXE, true);
                        }
                    }
                }

            } else if (ResourceNodeEntity.class.isAssignableFrom(hitResult.get().getClass())) {

                /*
                 * Resource Node Entity, for harvesting (Ex: Stone, Iron, Gold)
                 * */

                EntityHitResult hitResult1 = (EntityHitResult) hitResult;
                ResourceNodeEntity resourceNodeEntity = (ResourceNodeEntity) hitResult1.get();

                netConnection.disableInteraction(1500);

                DedicatedServer.get(ResourceService.class).harvest(hitResult1, resourceNodeEntity, netConnection);
            } else if (inHand != null && ToolInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {

                /*
                 * A Tool is in-hand, used for combat
                 * */

                netConnection.disableInteraction(1000);

                float DamageAmount = 1;
                ToolInventoryItem wepaon = (ToolInventoryItem) inHand.getItem();

                /*
                 * Animation callback for combat
                 * */
                netConnection.setAnimationCallback((connection, args) -> {
//                    connection.enableMovment();
                    if (inHand.getItem().tagEquals("weapon", "oneHand")) {
                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
                        DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, 300, true);
                    } else if (inHand.getItem().tagEquals("weapon", "twoHand")) {
                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
                        DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, 300, true);
                    } else if (inHand.getItem().tagEquals("weapon", "spear")) {
                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
                        DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.FORWARD, 400, true);
                    }
                });

//                netConnection.disableMovment();
                if (inHand.getItem().tagEquals("weapon", "oneHand")) {

                    /*
                     * One-Handed Tool
                     * */

                    if (netConnection.isFirstPerson()) {
                        netConnection.playMontage(Montage.OneHandCombo, 0.8F, 0.35F, netConnection.getOneHandedSlot());
                    } else {
                        netConnection.playMontage(Montage.OneHandCombo, 1.5F, 0.35F, netConnection.getOneHandedSlot());
                    }
                    return;

                } else if (inHand.getItem().tagEquals("weapon", "twoHand")) {

                    /*
                     * Two-Handed Tool
                     * */

                    if (netConnection.isFirstPerson()) {
                        netConnection.playMontage(Montage.TwoHandCombo, 0.8F, 0.35F, netConnection.getTwoHandedSlot());
                    } else {
                        netConnection.playMontage(Montage.TwoHandCombo, 1.5F, 0.35F, netConnection.getTwoHandedSlot());
                    }
                    return;
                } else if (inHand.getItem().tagEquals("weapon", "spear")) {

                    /*
                     * Spear Tool
                     * */

                    netConnection.playAnimation(Animation.SpearQuick, "DefaultSlot", 1.5F, 0, -1, 0.25f, 0.25f, true);
                    return;
                }

            }


        } else {

            /*
             * No Hit Result
             * */

            if (inHand != null && ToolInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {

                /*
                 * Tool In-Hand, used for combat
                 * */

                float DamageAmount = 1;
                ToolInventoryItem wepaon = (ToolInventoryItem) inHand.getItem();

                /*
                 * Animation callback for combat
                 * */
                netConnection.setAnimationCallback((connection, args) -> {
//                    connection.enableMovment();
                    if (inHand.getItem().tagEquals("weapon", "oneHand")) {
                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
                        DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, 300, true);
                    } else if (inHand.getItem().tagEquals("weapon", "twoHand")) {
                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
                        DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, 300, true);
                    } else if (inHand.getItem().tagEquals("weapon", "spear")) {
                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
                        DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.FORWARD, 400, true);
                    }
                });

                // Is a melee weapon
//                netConnection.disableMovment();
                if (inHand.getItem().tagEquals("weapon", "oneHand")) {
                    if (netConnection.isFirstPerson()) {
                        netConnection.playMontage(Montage.OneHandCombo, 0.8F, 0.35F, netConnection.getOneHandedSlot());
                    } else {
                        netConnection.playMontage(Montage.OneHandCombo, 1.5F, 0.35F, netConnection.getOneHandedSlot());
                    }
                    return;

                } else if (inHand.getItem().tagEquals("weapon", "twoHand")) {
                    if (netConnection.isFirstPerson()) {
                        netConnection.playMontage(Montage.TwoHandCombo, 0.8F, 0.35F, netConnection.getTwoHandedSlot());
                    } else {
                        netConnection.playMontage(Montage.TwoHandCombo, 1.5F, 0.35F, netConnection.getTwoHandedSlot());
                    }
                    return;
                } else if (inHand.getItem().tagEquals("weapon", "spear")) {
                    netConnection.playAnimation(Animation.SpearQuick, "DefaultSlot", 1.5F, 0, -1, 0.25f, 0.25f, true);
                    return;
                }

            }
        }


        /*
         * Process In-Hand Call
         * */
        if (netConnection.getPlayer().equipmentSlots.inHand != null) {
            if (UsableInventoryItem.class.isAssignableFrom(netConnection.getPlayer().equipmentSlots.inHand.getItem().getClass())) {
                // Is a usable item
                UsableInventoryItem ui = (UsableInventoryItem) netConnection.getPlayer().equipmentSlots.inHand.getItem();
                if (ui.onUse(netConnection, netConnection.getLookingAt(), InteractAction.PRIMARY, netConnection.getPlayer().equipmentSlots.inHand)) {
                    return;
                }
            }
        }
    }
}
