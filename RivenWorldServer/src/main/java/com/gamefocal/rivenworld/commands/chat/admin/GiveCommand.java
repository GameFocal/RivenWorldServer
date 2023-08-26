package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.service.InventoryService;

@Command(name = "give", sources = "chat,cli", aliases = "i,item")
public class GiveCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        HiveNetConnection sendTo = null;
        Class<? extends InventoryItem> itemClass = null;
        int amt = 1;

        if (source == CommandSource.CHAT && netConnection.isAdmin()) {

            // give {item} {amt}

            if (message.args.length >= 1) {
                String itemSlug = message.args[0];
                itemClass = DedicatedServer.get(InventoryService.class).getItemClassFromSpawnName(itemSlug);
                sendTo = netConnection;
                if(message.args.length >= 2) {
                    amt = Integer.parseInt(message.args[1]);
                }
            }

            netConnection.sendChatMessage(ChatColor.RED + "Command Error: /give {name} [amt]");
        } else if(source == CommandSource.CONSOLE) {

            // give {name} {item} {amt}

            if(message.args.length >= 3) {
                sendTo = DedicatedServer.getPlayerFromName(message.args[0]);
                itemClass = DedicatedServer.get(InventoryService.class).getItemClassFromSpawnName(message.args[1]);
                if(message.args.length >= 3) {
                    amt = Integer.parseInt(message.args[2]);
                }
            }
        }

        if(sendTo != null) {

            if(itemClass == null) {
                if(source == CommandSource.CHAT) {
                    sendTo.sendChatMessage("Invalid Item");
                } else {
                    System.out.println("Invalid item with name");
                }
                return;
            }

            InventoryItem item = (InventoryItem) itemClass.newInstance();

            if (item.isStackable() && !item.isHasDurability()) {
                sendTo.getPlayer().inventory.add((InventoryItem) itemClass.newInstance(), amt);
            } else {
                for (int i = 0; i < amt; i++) {
                    sendTo.getPlayer().inventory.add((InventoryItem) itemClass.newInstance(), 1);
                }
            }

            sendTo.sendChatMessage(ChatColor.GREEN + "Server: Giving you " + amt + " of " + item.getName());
            return;
        }
    }
}
