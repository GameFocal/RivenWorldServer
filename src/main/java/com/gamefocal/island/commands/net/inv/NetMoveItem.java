package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.inv.InventoryMoveEvent;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.service.InventoryService;

import java.util.UUID;

@Command(name = "invmv", sources = "tcp")
public class NetMoveItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // invmv|{inv}|{slot}|{amt}|{toinv}|{toslot}

        String fromInv = message.args[0];
        Integer fromSlot = Integer.valueOf(message.args[1]);
        Integer amt = Integer.valueOf(message.args[2]);
        String toInv = message.args[3];
        Integer toSlot = Integer.valueOf(message.args[4]);

        Inventory from = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(fromInv));
        Inventory to = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(toInv));

        if (from != null && to != null) {

            System.out.println("FROM: " + from.getUuid() + " TO: " + to.getUuid() + " Slot: " + toSlot);

            if (from.isOwner(netConnection) && to.isOwner(netConnection)) {

                if (!from.isEmpty(fromSlot)) {

                    if (to.isLocked()) {
                        return;
                    }

                    if (new InventoryMoveEvent(from, to, fromSlot, toSlot, amt).call().isCanceled()) {
                        return;
                    }

                    // Is not empty and has a item stack
                    InventoryStack fromStack = from.get(fromSlot);

                    if (to.isEmpty(toSlot)) {
                        to.set(toSlot, fromStack);
                        from.clear(fromSlot);
                    } else {
                        // Has something in it.

                        InventoryStack toStack = to.get(toSlot);
                        if (toStack.getHash().equalsIgnoreCase(fromStack.getHash())) {
                            // Is the same some be can combine
                            int newAmt = toStack.getAmount() + fromStack.getAmount();
                            int toMove = newAmt;

                            if (newAmt > to.getMaxStack()) {
                                toMove = to.getMaxStack();
                            }

                            toStack.setAmount(toMove);
                            fromStack.setAmount(newAmt - toMove);
                        } else {
                            // Swap the items
                            from.set(fromSlot, toStack);
                            to.set(toSlot,fromStack);
                        }

                    }

                    if (from.getUuid() == to.getUuid()) {
                        from.update();
                        netConnection.updateInventory(from);
                    } else {
                        // Emit Inventory Update
                        from.update();
                        to.update();

//                        netConnection.updateInventory(from);
                        netConnection.updateInventory(from);
                        Thread.sleep(50);
                        netConnection.updateInventory(to);

//                        netConnection.updateInventoryGUI(to);
////                        netConnection.updateInventoryGUI(from);
                    }
                }

            } else {
                System.err.println("Not the owner of one of these...");
            }

        }

    }
}
