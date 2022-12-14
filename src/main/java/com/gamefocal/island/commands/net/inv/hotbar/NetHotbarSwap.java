package com.gamefocal.island.commands.net.inv.hotbar;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.inventory.hotbar.PlayerHotbar;

import java.util.UUID;

@Command(name = "hotbswap")
public class NetHotbarSwap extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        int fromSlot = Integer.parseInt(message.args[0]);
        int toSlot = Integer.parseInt(message.args[1]);

        PlayerHotbar hotbar = netConnection.getPlayer().hotbar;

        if (hotbar.isLinked(fromSlot) && !hotbar.isLinked(toSlot)) {
            // To slot is empty
            UUID fromUUID = netConnection.getPlayer().hotbar.items[fromSlot];

            netConnection.getPlayer().hotbar.items[toSlot] = fromUUID;
            netConnection.getPlayer().hotbar.items[fromSlot] = null;
        } else if (hotbar.isLinked(fromSlot) && hotbar.isLinked(toSlot)) {
            // Something exist within the to slot

            UUID fromUUID = netConnection.getPlayer().hotbar.items[fromSlot];
            UUID toUUID = netConnection.getPlayer().hotbar.items[toSlot];

            netConnection.getPlayer().hotbar.items[fromSlot] = toUUID;
            netConnection.getPlayer().hotbar.items[toSlot] = fromUUID;
        }

        netConnection.syncHotbar();
    }
}
