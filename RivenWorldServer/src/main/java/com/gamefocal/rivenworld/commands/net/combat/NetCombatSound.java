package com.gamefocal.rivenworld.commands.net.combat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.service.CombatService;

@Command(name = "ncs", sources = "tcp")
public class NetCombatSound extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
//        InventoryStack inHand = netConnection.getPlayer().equipmentSlots.inHand;
//        if (inHand != null) {
//            if (inHand.getItem().tagEquals("weapon", "oneHand")) {
//                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
//
//            } else if (inHand.getItem().tagEquals("weapon", "twoHand")) {
//                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
//
//            } else if (inHand.getItem().tagEquals("weapon", "spear")) {
//                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);
//            }
//        }
    }
}
