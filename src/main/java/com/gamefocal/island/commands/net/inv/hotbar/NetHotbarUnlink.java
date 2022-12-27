package com.gamefocal.island.commands.net.inv.hotbar;

import com.gamefocal.island.entites.net.*;

@Command(name = "hotbunlink",sources = "tcp")
public class NetHotbarUnlink extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        int slot = Integer.parseInt(message.args[1]);

        if(netConnection.getPlayer().hotbar.isLinked(slot)) {
            netConnection.getPlayer().hotbar.items[slot] = null;

            netConnection.syncHotbar();
        }

    }
}
