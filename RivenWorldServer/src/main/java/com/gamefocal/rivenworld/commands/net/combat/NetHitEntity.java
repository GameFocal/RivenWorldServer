package com.gamefocal.rivenworld.commands.net.combat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.combat.CombatAngle;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.service.CombatService;

@Command(name = "nch", sources = "tcp")
public class NetHitEntity extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // Hit a entity
//        System.out.println(message.toString());

        String stance = message.args[0];
        String direction = message.args[1];

        if (!netConnection.canUseEnergy(15)) {
            netConnection.playSoundAtPlayer(GameSounds.TiredGasp, .5f, 1f);
            return;
        }

        netConnection.getPlayer().playerStats.energy -= 15;

        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.SWOOSH, netConnection.getPlayer().location, 250, .5f, 1f);

        DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.valueOf(direction), 150);

    }
}
