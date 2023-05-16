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

        if (!netConnection.canUseEnergy(15)) {
            netConnection.playSoundAtPlayer(GameSounds.TiredGasp, .5f, 1f);
            return;
        }
        InventoryStack inHand = netConnection.getPlayer().equipmentSlots.inHand;
        if (inHand != null) {
            if (inHand.getItem().tagEquals("weapon", "oneHand")) {
                netConnection.getPlayer().playerStats.energy -= 15;
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
                netConnection.playAnimation(Animation.oneHandHeavy, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, false);
                //TODO: add hit trace logic
            DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.LEFT, 20, true);

            } else if (inHand.getItem().tagEquals("weapon", "twoHand")) {
                netConnection.getPlayer().playerStats.energy -= 15;
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
                netConnection.playAnimation(Animation.twoHandHeavy, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, true);
                //TODO: add hit trace logic
//            DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.UPPER, range, true);

            } else if (inHand.getItem().tagEquals("weapon", "spear")) {
                netConnection.getPlayer().playerStats.energy -= 15;
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
                netConnection.playAnimation(Animation.SpearHeavy, "DefaultSlot", 1, 0, -1, 0.25f, 0.25f, true);
                //TODO: add hit trace logic
//            DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.FORWARD, range, true);
            }
        }
    }
}
