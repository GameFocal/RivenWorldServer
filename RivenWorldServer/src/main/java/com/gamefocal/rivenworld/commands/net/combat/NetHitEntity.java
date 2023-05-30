package com.gamefocal.rivenworld.commands.net.combat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.service.CombatService;
import org.checkerframework.checker.units.qual.A;

@Command(name = "nch", sources = "tcp")
public class NetHitEntity extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // Hit a entity
//        System.out.println(message.toString());

        System.out.println("HEAVY");

        if (netConnection.getAnimationCallback() != null) {
            System.out.println("BLOCKED DUE TO ANIM");
            return;
        }

        //OLD COMBAT LOGIC ----------------------------------------->
//
//        String stance = message.args[0];
//        String direction = message.args[1];
//
//        if (!netConnection.canUseEnergy(15)) {
//            netConnection.playSoundAtPlayer(GameSounds.TiredGasp, .5f, 1f);
//            return;
//        }
//
//        netConnection.getPlayer().playerStats.energy -= 15;
//
//        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
//
//        DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.valueOf(direction), 150,false);

        //OLD COMBAT LOGIC ----------------------------------------->

//        if (!netConnection.canUseEnergy(30)) {
//            netConnection.playSoundAtPlayer(GameSounds.TiredGasp, .5f, 1f);
//            return;
//        }

        InventoryStack inHand = netConnection.getPlayer().equipmentSlots.inHand;
        if (inHand != null) {

            netConnection.setAnimationCallback((connection, args) -> {
                netConnection.enableMovment();
                if (inHand.getItem().tagEquals("weapon", "oneHand")) {
                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
                    DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, 300, false);
                } else if (inHand.getItem().tagEquals("weapon", "twoHand")) {
                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
                    DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, 300, false);
                } else if (inHand.getItem().tagEquals("weapon", "spear")) {
                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
                    DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, 400, false);
                }
            });

            netConnection.disableMovment();

//            netConnection.getPlayer().playerStats.energy -= 30;
            if (inHand.getItem().tagEquals("weapon", "oneHand")) {
                netConnection.playAnimation(Animation.oneHandHeavy, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, false);
//                DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, 300, false);
//                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);

            } else if (inHand.getItem().tagEquals("weapon", "twoHand")) {
                netConnection.playAnimation(Animation.twoHandHeavy, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, true);
//                DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, 300, false);
//                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);

            } else if (inHand.getItem().tagEquals("weapon", "spear")) {
                netConnection.playAnimation(Animation.SpearHeavy, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, true);
//                DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.RIGHT, 400, false);
//                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
            }
        }
    }
}
