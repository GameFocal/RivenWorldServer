package com.gamefocal.island.commands.net.inv.crafting;

import com.gamefocal.island.entites.net.*;

@Command(name = "invcs")
public class NetCraftItem extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println(message.toString());

    }
}
