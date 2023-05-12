package com.gamefocal.rivenworld.commands.net.combat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.items.weapons.MeleeWeapon;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.player.Montage;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;
import com.gamefocal.rivenworld.game.ray.hit.FoliageHitResult;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.service.CombatService;
import com.gamefocal.rivenworld.service.FoliageService;
import com.gamefocal.rivenworld.service.ResourceService;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Command(name = "nchq", sources = "tcp")
public class NetHitEntityQuickAttack extends HiveCommand {

    public static ConcurrentHashMap<UUID, Long> lastHit = new ConcurrentHashMap<>();

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        if (lastHit.containsKey(netConnection.getUuid())) {
            Long hitLast = lastHit.get(netConnection.getUuid());
            if ((System.currentTimeMillis() - hitLast) <= 300) {
                return;
            }
        }
//        float quickCnt = Float.parseFloat(message.args[0]);
        lastHit.put(netConnection.getUuid(), System.currentTimeMillis());
        System.out.println(message);

        InventoryStack inHand = netConnection.getPlayer().equipmentSlots.inHand;
        HitResult hitResult = netConnection.getLookingAt();

        if (inHand != null && MeleeWeapon.class.isAssignableFrom(inHand.getItem().getClass())) {
            float DamageAmount = 1;
            ToolInventoryItem wepaon = (ToolInventoryItem) inHand.getItem();
            // Is a melee weapon

            if (inHand.getItem().tagEquals("weapon", "oneHand")) {
                netConnection.playMontage(Montage.OneHandCombo, 1.5F, 0.35F);
//                DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, range, true);

            } else if (inHand.getItem().tagEquals("weapon", "twoHand")) {
                netConnection.playMontage(Montage.TwoHandCombo, 1.5F, 0.35F);
//                DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, range, true);
            } else if (inHand.getItem().tagEquals("weapon", "spear")) {
                netConnection.playAnimation(Animation.SpearQuick, "DefaultSlot", 1.5F, 0, -1, 0.25f, 0.25f, true);
//                DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, range, true);
            }
        }

        if (inHand == null) {
            netConnection.playAnimation(Animation.PUNCH, "UpperBody", 1, 0, -1, 0.25f, 0.25f, true);
//            DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.FORWARD, 100, true);
        }

        if (hitResult != null && FoliageHitResult.class.isAssignableFrom(hitResult.getClass())) {
            /*
             * Hit a tree
             * */


            if (inHand != null && Hatchet.class.isAssignableFrom(inHand.getItem().getClass())) {

                if (!netConnection.canUseEnergy(15)) {
                    netConnection.playSoundAtPlayer(GameSounds.TiredGasp, .5f, 1f);
                    return;
                }

                netConnection.getPlayer().playerStats.energy -= 15;
            } else {

                if (!netConnection.canUseEnergy(25)) {
                    netConnection.playSoundAtPlayer(GameSounds.TiredGasp, .5f, 1f);
                    return;
                }

                netConnection.getPlayer().playerStats.energy -= 25;
            }

            FoliageHitResult foliageHitResult = (FoliageHitResult) hitResult;
            DedicatedServer.get(FoliageService.class).harvest(foliageHitResult, netConnection);

        } else if (hitResult != null && EntityHitResult.class.isAssignableFrom(hitResult.getClass())) {
            /*
             * Entity Hit Result
             * */

            if (ResourceNodeEntity.class.isAssignableFrom(hitResult.get().getClass())) {
                // Is a Resource Node Entity

                EntityHitResult hitResult1 = (EntityHitResult) hitResult;
                ResourceNodeEntity resourceNodeEntity = (ResourceNodeEntity) hitResult1.get();

                if (!netConnection.canUseEnergy(15)) {
                    netConnection.playSoundAtPlayer(GameSounds.TiredGasp, .2f, 1f);
                    return;
                }

                netConnection.getPlayer().playerStats.energy -= 15;

                DedicatedServer.get(ResourceService.class).harvest(hitResult1, resourceNodeEntity, netConnection);
            } else {
                /*
                 * Quick attack
                 * */
                    // OLD COMBAT----------------------->
//                float range = 100;
//
//                if (inHand != null && inHand.getItem().tagEquals("weapon", "oneHand")) {
//                    netConnection.playAnimation(Animation.oneHandQuick);
//                    System.out.println("one hand");
//                    DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, range, true);
//                } else if (inHand != null && inHand.getItem().tagEquals("weapon", "twoHand")) {
//                    netConnection.playAnimation(Animation.twoHandQuick);
//                    range = 150;
//                    System.out.println("two hand");
//                    DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.UPPER, range, true);
//                } else if (inHand != null && inHand.getItem().tagEquals("weapon", "spear")) {
//                    netConnection.playAnimation(Animation.SpearQuick);
//                    range = 200;
//                    System.out.println("spear");
//                    DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.FORWARD, range, true);
//                }
//
////                DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, range, true);
                // OLD COMBAT----------------------->

                if (inHand != null && MeleeWeapon.class.isAssignableFrom(inHand.getItem().getClass())) {
                    float DamageAmount = 1;
                    ToolInventoryItem wepaon = (ToolInventoryItem) inHand.getItem();
                    // Is a melee weapon

                    if (inHand.getItem().tagEquals("weapon", "oneHand")) {
                        netConnection.playMontage(Montage.OneHandCombo, 1.5F, 0.35F);
//                        if (quickCnt == 0){
//                            netConnection.playAnimation(Animation.oneHandQuick, "DefaultSlot", 1.5F, 0, 2, 0.25f, 0.25f, true);
////                    DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, range, true);
//                            return;
//                        } else if (quickCnt == 1){
//                            netConnection.playAnimation(Animation.oneHandQuick, "DefaultSlot", 1.5F, 1.3F, 1.5F, 0.25f, 0.25f, true);
////                    DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, range, true);
//                            return;
//                        }
//                        else if (quickCnt == 2){
//                            netConnection.playAnimation(Animation.oneHandQuick, "DefaultSlot", 1.5F, 2.3F, -1, 0.25f, 0.25f, true);
////                    DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, range, true);
//                            return;
//                        }

                    } else if (inHand.getItem().tagEquals("weapon", "twoHand")) {
                        netConnection.playMontage(Montage.TwoHandCombo, 1.5F, 0.35F);
//                        DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, range, true);
                    } else if (inHand.getItem().tagEquals("weapon", "spear")) {
                        netConnection.playAnimation(Animation.SpearQuick, "DefaultSlot", 1.5F, 0, -1, 0.25f, 0.25f, true);
//                        DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, range, true);
                    }

                }

            }

        } else {
            /*
             * Nothing in Hit Result
             * */
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

//        if (inHand != null) {
//            // Something is here
//
//            if (hitResult != null && Hatchet.class.isAssignableFrom(inHand.getItem().getClass())) {
//
//                // Is a Hatchet
//                if (FoliageHitResult.class.isAssignableFrom(hitResult.getClass())) {
//
//                    if (!netConnection.canUseEnergy(15)) {
//                        netConnection.playSoundAtPlayer(GameSounds.TiredGasp, .5f, 1f);
//                        return;
//                    }
//
//                    netConnection.getPlayer().playerStats.energy -= 15;
//
//                    FoliageHitResult foliageHitResult = (FoliageHitResult) hitResult;
//                    DedicatedServer.get(FoliageService.class).harvest(foliageHitResult, netConnection);
//                }
//
//
//            } else if (MeleeWeapon.class.isAssignableFrom(inHand.getItem().getClass())) {
//                float DamageAmount = 1;
//                ToolInventoryItem wepaon = (ToolInventoryItem) inHand.getItem();
//                // Is a melee weapon
//
//                float range = 100;
//
//                if (inHand.getItem().tagEquals("weapon", "oneHand")) {
//                    netConnection.playAnimation(Animation.oneHandQuick);
//                } else if (inHand.getItem().tagEquals("weapon", "twoHand")) {
//                    netConnection.playAnimation(Animation.twoHandQuick);
//                    range = 150;
//                } else if (inHand.getItem().tagEquals("weapon", "spear")) {
//                    netConnection.playAnimation(Animation.SpearQuick);
//                    range = 200;
//                }
//
//                DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, range,true);
//
////                if (hitResult != null) {
////                    if (PlayerHitResult.class.isAssignableFrom(hitResult.getClass())) {
////                        PlayerHitResult playerHitResult = (PlayerHitResult) hitResult;
////                        HiveNetConnection connection = playerHitResult.get();
////                        if (netConnection.getPlayer().location.dist(connection.getPlayer().location) <= 100) {
////                            connection.takeDamage(DamageAmount);
////                        }
////                    }
////                }
//
//            } else if (hitResult != null && EntityHitResult.class.isAssignableFrom(hitResult.getClass())) {
//                // Is a entity hit result
//
//                if (ResourceNodeEntity.class.isAssignableFrom(hitResult.get().getClass())) {
//                    // Is a Resource Node Entity
//
//                    EntityHitResult hitResult1 = (EntityHitResult) hitResult;
//                    ResourceNodeEntity resourceNodeEntity = (ResourceNodeEntity) hitResult1.get();
//
//                    if (!netConnection.canUseEnergy(15)) {
//                        netConnection.playSoundAtPlayer(GameSounds.TiredGasp, .5f, 1f);
//                        return;
//                    }
//
//                    netConnection.getPlayer().playerStats.energy -= 15;
//
//                    DedicatedServer.get(ResourceService.class).harvest(hitResult1, resourceNodeEntity, netConnection);
//                }
//            }
//
//            /*
//             * Process in-hand call
//             * */
//            if (netConnection.getPlayer().equipmentSlots.inHand != null) {
//                if (UsableInventoryItem.class.isAssignableFrom(netConnection.getPlayer().equipmentSlots.inHand.getItem().getClass())) {
//                    // Is a usable item
//                    UsableInventoryItem ui = (UsableInventoryItem) netConnection.getPlayer().equipmentSlots.inHand.getItem();
//                    if (ui.onUse(netConnection, netConnection.getLookingAt(), InteractAction.PRIMARY, netConnection.getPlayer().equipmentSlots.inHand)) {
//                        return;
//                    }
//                }
//            }
//        } else {
//
//            if (!netConnection.canUseEnergy(5)) {
//                netConnection.playSoundAtPlayer(GameSounds.TiredGasp, .5f, 1f);
//                return;
//            }
//
//            netConnection.getPlayer().playerStats.energy -= 5;
//
//            netConnection.playAnimation(Animation.PUNCH);
//            if (hitResult != null) {
//                if (PlayerHitResult.class.isAssignableFrom(hitResult.getClass())) {
//                    PlayerHitResult playerHitResult = (PlayerHitResult) hitResult;
//                    HiveNetConnection connection = playerHitResult.get();
//                    if (netConnection.getPlayer().location.dist(connection.getPlayer().location) <= 100) {
//                        connection.takeDamage(1);
//                    }
//                }
//            }
//        }
    }
}
