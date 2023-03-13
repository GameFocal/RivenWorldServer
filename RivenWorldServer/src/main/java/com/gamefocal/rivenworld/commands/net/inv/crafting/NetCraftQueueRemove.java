package com.gamefocal.rivenworld.commands.net.inv.crafting;

import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.ui.CraftingUI;
import com.gamefocal.rivenworld.game.ui.GameUI;

@Command(name = "invcci", sources = "tcp")
public class NetCraftQueueRemove extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (message.args.length == 1) {
            GameUI openUi = netConnection.getOpenUI();
            if (openUi != null) {
                if (CraftingUI.class.isAssignableFrom(openUi.getClass())) {
                    CraftingUI craftingUI = (CraftingUI) openUi;
                    craftingUI.queue().cancelBySlotNumber(Integer.parseInt(message.args[0]));
                    netConnection.updateCraftingUI();
                }
            }
        }
    }
}
