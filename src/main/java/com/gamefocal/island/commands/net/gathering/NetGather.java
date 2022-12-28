package com.gamefocal.island.commands.net.gathering;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.tasks.HiveTaskSequence;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.service.ForageService;
import com.gamefocal.island.service.TaskService;

import java.util.List;

@Command(name = "ng", sources = "tcp")
public class NetGather extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // ng|type|loc|data

        String type = message.args[0];
        Location location = Location.fromString(message.args[1]);
        String misc = message.args[2];

        System.out.println(message.toString());

        if (type.equalsIgnoreCase("terrain")) {
            // Forage from the ground.

            List<InventoryStack> stacks = DedicatedServer.get(ForageService.class).forageGround(netConnection, misc, location);

            HiveTaskSequence sequence = new HiveTaskSequence(false);
            sequence.await(20L);

            for (InventoryStack s : stacks) {
                netConnection.getPlayer().inventory.add(s);
                sequence.exec(() -> {
                    netConnection.displayItemAdded(s);
                });
            }
            sequence.await(5L);
            sequence.exec(() -> {
                netConnection.updateInventory(netConnection.getPlayer().inventory);
            });

            TaskService.scheduleTaskSequence(sequence);

        } else if (type.equalsIgnoreCase("foliage")) {
            // Forage from a tree
        }

    }
}
