package com.gamefocal.rivenworld.commands.net.inv;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.inv.InventoryItemClickEvent;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryClick;
import com.gamefocal.rivenworld.service.InventoryService;

import java.util.UUID;

@Command(name = "invc", sources = "tcp")
public class NetInventoryClick extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        Inventory inv = DedicatedServer.get(InventoryService.class).getInvFromId(UUID.fromString(message.args[0]));
        int slot = Integer.parseInt(message.args[1]);
        String ac = message.args[2];

        InventoryClick click = null;

        switch (ac) {
            case "LC":
                click = InventoryClick.LEFT_CLICK;
                break;
            case "MC":
                click = InventoryClick.MIDDLE_CLICK;
                break;
            case "RC":
                click = InventoryClick.RIGHT_CLICK;
                break;
        }

        if (inv != null && slot >= 0 && ac != null) {

            InventoryItemClickEvent clickEvent = new InventoryItemClickEvent(inv, inv.get(slot), slot, netConnection, click).call();

            if (clickEvent.isCanceled()) {
                // Set the cancel event for this item.
                synchronized (InventoryService.blockedSlots) {
                    InventoryService.blockedSlots.add(netConnection.getUuid());
                }
            }
        }
    }
}
