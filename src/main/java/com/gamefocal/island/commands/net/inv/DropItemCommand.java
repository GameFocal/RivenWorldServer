package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.entites.storage.DropBag;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;

import java.util.List;

@Command(name = "invdrop", sources = "tcp")
public class DropItemCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // invdrop|{netid}|{index}|{amt}

        // Has a inventory open

        System.out.println(message.toString());

        int slot = Integer.parseInt(message.args[0]);
        int amt = Integer.parseInt(message.args[1]);

        Inventory inventory = netConnection.getOpenInventory();

        if (inventory != null) {
            // Has inventory

            if (!inventory.isEmpty(slot)) {
                // Not empty

                InventoryStack stack = inventory.get(slot);
                if (stack != null) {
                    // Has a item here

                    int toDrop = amt;
                    if (amt > stack.getAmount()) {
                        toDrop = stack.getAmount();
                    }

                    stack.setAmount(stack.getAmount() - toDrop);

                    InventoryStack newStack = new InventoryStack(stack.getItem(), toDrop);

                    // Find if a bag is nearby
                    List<DropBag> bags = DedicatedServer.instance.getWorld().getEntitesOfTypeWithinRadius(DropBag.class, netConnection.getPlayer().location, 5);

                    boolean isPlaced = false;
                    for (DropBag b : bags) {
                        if (b.getDroppedBy() == netConnection.getUuid()) {
                            // Is the same player
                            if (b.getInventory().canAdd(newStack)) {
                                b.getInventory().add(newStack);
                                isPlaced = true;
                                break;
                            }
                        }
                    }

                    if (!isPlaced) {
                        // Spawn a new bag here
                        DedicatedServer.instance.getWorld().spawn(new DropBag(netConnection, newStack), netConnection.getPlayer().location);
                    }

                    netConnection.getPlayer().inventory.update();
                    netConnection.updateInventory(netConnection.getPlayer().inventory);
                }
            }
        }
    }
}
