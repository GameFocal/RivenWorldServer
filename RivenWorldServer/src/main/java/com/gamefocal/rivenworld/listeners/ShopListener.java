package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.entites.econ.Shop;
import com.gamefocal.rivenworld.entites.econ.ShopItem;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.inv.InventoryItemClickEvent;
import com.gamefocal.rivenworld.game.inventory.InventoryClick;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.resources.econ.GoldCoin;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.ui.inventory.RivenShopUI;
import com.gamefocal.rivenworld.service.ShopService;

public class ShopListener implements EventInterface {

    @EventHandler
    public void onInventoryItemClickEvent(InventoryItemClickEvent event) {
        if (event.getItem().getItem().getData().getTags().containsKey("shopId")) {
            // Is a shop item

            System.out.println("SHOP ITEM");
            event.setCanceled(true);

            RivenShopUI shopUI = (RivenShopUI) event.getBy().getOpenUI();
            Shop shop = shopUI.getAttached();

            HiveNetConnection connection = event.getBy();

            if (shop != null) {

                ShopItem i = shop.getItemByClass(event.getItem().getItem().getClass());

                Class<? extends InventoryItem> c = i.getItem();

                if (event.getClick() == InventoryClick.LEFT_CLICK) {
                    // BUY

                    if (i.getAmt() > 0) {
                        // Can buy

                        // Can afford
                        int coins = ShopService.getCoins(connection.getPlayer().inventory);
                        if (coins >= i.getSell()) {
                            i.setAmt(i.getAmt() - 1);

                            connection.getPlayer().inventory.removeOfType(GoldCoin.class, i.getSell());
                            try {
                                connection.getPlayer().inventory.add(i.getItem().newInstance(), 1);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }

                            shopUI.update(connection);
                            connection.playWorldSoundAtPlayerLocation(GameSounds.CashRegister, .5f, 1, 2);

                        } else {
                            connection.sendChatMessage(ChatColor.RED + "You do not have enough coins for this.");
                        }
                    } else {
                        connection.sendChatMessage(ChatColor.RED + "This shop is sold out.");
                    }


                } else if (event.getClick() == InventoryClick.RIGHT_CLICK) {
                    // SELL

                    // Check the player inventory
                    if (connection.getPlayer().inventory.getOfType(c).size() > 0) {

                        connection.getPlayer().inventory.removeOfType(c, 1);
                        connection.getPlayer().inventory.update();

                        i.setAmt(i.getAmt() + 1);

                        // Pay the player
                        connection.getPlayer().inventory.add(new GoldCoin(), i.getBuy());
                        connection.playWorldSoundAtPlayerLocation(GameSounds.CashRegister, .5f, 1, 2);

                        shopUI.update(connection);
                    } else {
                        connection.sendChatMessage(ChatColor.RED + "You do not have enough of this item to sell this.");
                    }

                }

            }

        }
    }

}
