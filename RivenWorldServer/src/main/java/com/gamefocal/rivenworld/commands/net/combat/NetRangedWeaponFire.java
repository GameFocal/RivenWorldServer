package com.gamefocal.rivenworld.commands.net.combat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.weapons.RangedWeapon;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.service.CombatService;

@Command(name = "nrwf", sources = "tcp")
public class NetRangedWeaponFire extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
//        System.out.println(message.toString());

        if (netConnection.getPlayer().equipmentSlots.inHand != null) {

            InventoryStack inHand = netConnection.getPlayer().equipmentSlots.inHand;

            if (RangedWeapon.class.isAssignableFrom(inHand.getItem().getClass())) {
                // TODO: Add a ammo check here soon-ish

                // Sound effects
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.BOW_FIRE, netConnection.getPlayer().location, 5000, 1, 1);

                DedicatedServer.get(CombatService.class).rangedHitResult(netConnection, netConnection.getCameraLocation(), Float.parseFloat(message.args[0]), 1);
            }
        }
    }
}
