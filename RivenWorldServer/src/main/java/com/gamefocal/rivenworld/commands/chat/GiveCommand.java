package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.service.InventoryService;

@Command(name = "give", sources = "chat", aliases = "i,item")
public class GiveCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
//
//        // give {item} {amt}
//
//        if (message.args.length >= 1) {
//
//            String itemSlug = message.args[0];
//
//            if(itemSlug.equalsIgnoreCase("devkit")) {
//                // Spawn the devkit
//                // TODO: Remove this before ship
//
//
//            }
//
//            Class ic = DedicatedServer.get(InventoryService.class).getItemClassFromSpawnName(itemSlug);
//
//            if (ic == null) {
//                netConnection.sendChatMessage(ChatColor.RED + "Server: Unknown Item Code '" + itemSlug + "'");
//                return;
//            }
//
//            int amt = 1;
//            if (message.args.length == 2) {
//                amt = Integer.parseInt(message.args[1]);
//            }
//
//            netConnection.getPlayer().inventory.add((InventoryItem) ic.newInstance(), amt);
//
//            netConnection.sendChatMessage(ChatColor.GREEN + "Server: Giving you " + amt + " of " + itemSlug);
//            return;
//        }
//
//        netConnection.sendChatMessage(ChatColor.RED + "Command Error: /give {name} [amt]");
    }
}
