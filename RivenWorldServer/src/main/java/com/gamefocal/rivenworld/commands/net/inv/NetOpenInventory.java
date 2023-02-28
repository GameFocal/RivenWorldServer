package com.gamefocal.rivenworld.commands.net.inv;

import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.ui.inventory.PlayerInventoryUI;
import com.gamefocal.rivenworld.game.ui.inventory.RivenInventoryUI;

@Command(name = "invopen", sources = "tcp")
public class NetOpenInventory extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
//        PlayerInventoryUI ui = new PlayerInventoryUI();
//        netConnection.getPlayer().inventory.setLinkedUI(ui);
//        ui.open(netConnection, netConnection.getPlayer().inventory);
//        netConnection.openInventory(netConnection.getPlayer().inventory, true);

        RivenInventoryUI ui = new RivenInventoryUI();
        ui.open(netConnection, netConnection.getPlayer().inventory);

    }
}
