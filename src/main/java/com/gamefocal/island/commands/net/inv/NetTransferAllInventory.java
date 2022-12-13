package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.tasks.seqence.ExecSequenceAction;
import com.gamefocal.island.game.tasks.seqence.WaitSequenceAction;
import com.gamefocal.island.service.InventoryService;
import com.gamefocal.island.service.TaskService;

import java.util.UUID;

@Command(name = "invtra", sources = "tcp")
public class NetTransferAllInventory extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // invtra|{from}|{to}

        System.out.println(message);

        String fromInventoryId = message.args[0];
        String toInventoryId = message.args[1];

        Inventory from = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(fromInventoryId));
        Inventory to = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(toInventoryId));

        if (from != null && to != null && !fromInventoryId.equalsIgnoreCase(toInventoryId)) {
            // Are valid inventories

            if (from.isOwner(netConnection) && to.isOwner(netConnection)) {
                // Owner of both

                if(to.isLocked()) {
                    return;
                }

                int i = 0;
                for (InventoryStack stack : from.getItems()) {
                    if (to.canAdd(stack)) {
                        to.add(stack);
                        from.clear(i);
                    }
                    i++;
                }

                to.update();
                from.update();

                TaskService.scheduleTaskSequence(false, new ExecSequenceAction() {
                    @Override
                    public void run() {
                        netConnection.updateInventory(to);
                    }
                }, new WaitSequenceAction(1L), new ExecSequenceAction() {
                    @Override
                    public void run() {
                        netConnection.updateInventory(from);
                    }
                });
            }

        }

    }
}
