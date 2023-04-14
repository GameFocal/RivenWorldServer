package com.gamefocal.rivenworld.commands.net.inv;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.tasks.seqence.ExecSequenceAction;
import com.gamefocal.rivenworld.game.tasks.seqence.WaitSequenceAction;
import com.gamefocal.rivenworld.service.InventoryService;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.UUID;

@Command(name = "invtri", sources = "tcp")
public class NetTransferItems extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // invtri|{from}|{to}

//        System.out.println(message);

        String type = message.args[0];
        String objSlug = message.args[1];
        int customAmt = Integer.parseInt(message.args[2]);
        String fromId = message.args[3];
        String toId = message.args[4];
        int fromSlot = Integer.parseInt(message.args[5]);

        Inventory from = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(fromId));
        Inventory to = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(toId));

        if (from != null && to != null && !toId.equalsIgnoreCase(fromId)) {
            // Are valid inventories

            if (from.isOwner(netConnection) && to.isOwner(netConnection)) {
                // Owner of both

                if (to.isLocked()) {
                    return;
                }

                if (type.equalsIgnoreCase("Transfer One")) {
                    // Transfer One

                    InventoryStack fromStack = from.get(fromSlot);
                    if (fromStack != null && fromStack.getAmount() >= 1) {
                        if (to.canAdd(new InventoryStack(fromStack.getItem(), 1))) {
                            to.add(fromStack.getItem(), 1);
                            fromStack.remove(1);
                        }
                    }

                } else if (type.equalsIgnoreCase("Transfer Stack")) {
                    // Transfer all of stack

                    InventoryStack fromStack = from.get(fromSlot);
                    if (fromStack != null) {
                        if (to.canAdd(fromStack)) {
                            to.add(fromStack);
                            from.clear(fromSlot);
                        }
                    }

                } else if (type.equalsIgnoreCase("Transfer all of type")) {
                    // Transfer all of this type

                    Class<? extends InventoryItem> itemClass = DedicatedServer.get(InventoryService.class).getItemClassFromSlug(objSlug);
                    if (itemClass != null) {

                        for (InventoryStack s : from.getOfType(itemClass)) {
                            // Attempt to transfer this
                            if (to.canAdd(s)) {
                                to.add(s);
                                s.clear();
                            }
                        }

                    }

                } else if (type.equalsIgnoreCase("Transfer Custom")) {
                    // Transfer Custom Amt

                    InventoryStack fromStack = from.get(fromSlot);
                    if (fromStack != null && fromStack.getAmount() >= customAmt) {
                        if (to.canAdd(new InventoryStack(fromStack.getItem(), customAmt))) {
                            to.add(fromStack.getItem(), customAmt);
                            fromStack.remove(customAmt);
                        }
                    }

                }

                /*
                 * Update the inventory here
                 * */
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
