package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;

@Command(name = "invdrop", sources = "tcp")
public class DropItemCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // invdrop|{index}|{amt}

        // Has a inventory open

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

                    stack.setAmount(stack.getAmount()-toDrop);

                    // TODO: Drop Action
                    // TODO: Spawn a drop bag at players feet through network entity manager

                }

            }

        }
    }
}
