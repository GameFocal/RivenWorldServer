package com.gamefocal.island.commands.net.combat;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.combat.CombatAngle;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.service.CombatService;

@Command(name = "nchq", sources = "tcp")
public class NetHitEntityQuickAttack extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // Hit a entity
//        System.out.println(message.toString());

        System.out.println(message);

//        String stance = message.args[0];
//        String direction = message.args[1];
//
//        DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.valueOf(direction), 150);

    }
}
