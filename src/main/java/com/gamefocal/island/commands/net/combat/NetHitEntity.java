package com.gamefocal.island.commands.net.combat;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.combat.CombatAngle;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.CombatService;

import java.util.UUID;

@Command(name = "nch", sources = "tcp")
public class NetHitEntity extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // Hit a entity
//        System.out.println(message.toString());

        String stance = message.args[0];
        String direction = message.args[1];

        DedicatedServer.get(CombatService.class).meleeHitResult(netConnection, CombatAngle.valueOf(direction), 150f);

    }
}
