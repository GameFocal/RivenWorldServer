package com.gamefocal.island.commands.net.inv;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.ui.inventory.PlayerInventoryUI;

@Command(name = "invopen", sources = "tcp")
public class NetOpenInventory extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        PlayerInventoryUI ui = new PlayerInventoryUI();
        ui.open(netConnection, netConnection.getPlayer().inventory);
//        netConnection.openInventory(netConnection.getPlayer().inventory, true);
    }
}
