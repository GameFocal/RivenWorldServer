package com.gamefocal.island.commands.net.combat;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.combat.CombatAngle;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.service.CombatService;

@Command(name = "nchq", sources = "tcp")
public class NetHitEntityQuickAttack extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

    }
}
