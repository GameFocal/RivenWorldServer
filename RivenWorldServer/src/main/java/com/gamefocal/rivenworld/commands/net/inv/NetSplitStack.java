package com.gamefocal.rivenworld.commands.net.inv;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.tasks.seqence.ExecSequenceAction;
import com.gamefocal.rivenworld.service.InventoryService;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.UUID;

@Command(name = "invspl", sources = "tcp")
public class NetSplitStack extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println(message);

        String invId = message.args[0];
        String splitAction = message.args[1];
        String objSlug = message.args[2];
        int customAmt = Integer.parseInt(message.args[3]);
        int fromSlot = Integer.parseInt(message.args[4]);

        Inventory inventory = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(invId));

        if (inventory != null && inventory.isOwner(netConnection)) {
            // Is a valid inventory and the user is the owner of it currently.

            InventoryStack fromStack = inventory.get(fromSlot);

            if (splitAction.equalsIgnoreCase("Split One")) {
                // Split one off
                if (fromStack.getAmount() >= 1 && inventory.hasEmptySlot()) {
                    fromStack.remove(1);
                    inventory.addToEmptySlot(new InventoryStack(fromStack.getItem(), 1));
                }

            } else if (splitAction.equalsIgnoreCase("Split Half")) {
                // Split in half

                int halfCount = fromStack.getAmount() / 2;
                if (inventory.hasEmptySlot()) {
                    fromStack.remove(halfCount);
                    inventory.addToEmptySlot(new InventoryStack(fromStack.getItem(), halfCount));
                }

            } else if (splitAction.equalsIgnoreCase("Split All")) {
                // Split into as many as we can

                while (inventory.hasEmptySlot() && fromStack.getAmount() > 1) {
                    fromStack.remove(1);
                    inventory.addToEmptySlot(new InventoryStack(fromStack.getItem(), 1));
                }

            } else if (splitAction.equalsIgnoreCase("Split Custom")) {
                // Split by custom input
                if (fromStack.getAmount() >= customAmt && inventory.hasEmptySlot()) {
                    fromStack.remove(customAmt);
                    inventory.addToEmptySlot(new InventoryStack(fromStack.getItem(), customAmt));
                }
            }

            /*
             * Update the inventory here
             * */
            inventory.update();

            TaskService.scheduleTaskSequence(false, new ExecSequenceAction() {
                @Override
                public void run() {
                    netConnection.updateInventory(inventory);
                }
            });

        }

    }
}
