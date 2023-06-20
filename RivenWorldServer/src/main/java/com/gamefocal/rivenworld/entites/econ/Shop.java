package com.gamefocal.rivenworld.entites.econ;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.living.NPC;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.service.EnvironmentService;
import com.gamefocal.rivenworld.service.ShopService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.LinkedList;

public class Shop {

    private String uid;

    private String name;

    private LinkedList<ShopItem> items = new LinkedList<>();

    private float open = -1;

    private float close = -1;

    public Shop(String uid, String name) {
        this.name = name;
        this.uid = uid;
    }

    public Shop(String uid, String name, ShopItem... items) {
        this.name = name;
        this.uid = uid;
        this.items.addAll(Arrays.asList(items));
    }

    public void addShopItem(ShopItem item) {
        this.items.add(item);
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<ShopItem> getItems() {
        return items;
    }

    public ShopItem getItemByClass(Class<? extends InventoryItem> c) {
        for (ShopItem i : this.items) {
            if (i.getItem().isAssignableFrom(c)) {
                return i;
            }
        }

        return null;
    }

    public void setItems(LinkedList<ShopItem> items) {
        this.items = items;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public Inventory buildShopInventory(HiveNetConnection connection) {
        Inventory inventory = new Inventory(this.items.size());
        inventory.setShowZeroItems(true);
        inventory.setLocked(true);

        for (ShopItem i : this.items) {

            boolean canBuyFromShop = true;
            boolean canSellToShop = true;
            String buyMsg = "";
            String sellMsg = "";

            int coins = ShopService.getCoins(connection.getPlayer().inventory);
            if (coins <= i.getSell()) {
                canBuyFromShop = false;
                buyMsg = "Not Enough Coins";
            } else if (i.getAmt() <= 0) {
                canBuyFromShop = false;
                buyMsg = "Not in Stock";
            }

            if (connection.getPlayer().inventory.amtOfType(i.getItem()) <= 0) {
                canSellToShop = false;
            }

            ChatColor buyColor = ChatColor.GREEN;
            if (!canBuyFromShop) {
                buyColor = ChatColor.RED;
            }

            ChatColor sellColor = ChatColor.GREEN;
            if (!canSellToShop) {
                sellColor = ChatColor.RED;
            }

            try {
                InventoryItem ti = i.getItem().newInstance();
                ti.setHasDurability(false);

                if (!canBuyFromShop && !canSellToShop) {
                    ti.setTint(Color.SALMON);
                }

                ti.attr(ChatColor.BOLD + "" + i.getAmt() + " In Stock");

                ti.attr("~~~~~~~~~~~~");

                ti.attr(ChatColor.ORANGE + "BUY For " + i.getSell() + " Coins");
                if (!canBuyFromShop) {
                    ti.attr(ChatColor.SMALL + "" + buyColor + "" + buyMsg);
                } else {
                    ti.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "" + buyColor + "Left Click to Buy 1 " + ti.getName());
                }

                ti.attr("~~~~~~~~~~~~");

                ti.attr(ChatColor.ORANGE + "SELL For " + i.getBuy() + " Coins");
                if (!canSellToShop) {
                    ti.attr(ChatColor.SMALL + "" + sellColor + "You don't have this item");
                } else {
                    ti.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "" + sellColor + "Right Click to Sell 1 " + ti.getName());
                }

                ti.attr("~~~~~~~~~~~~");

//                ti.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "Left Click to Buy");
//                ti.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "Right Click to Sell");

                ti.tag("shopId", this.uid);
                ti.tag("item", i.getItem().getSimpleName());

                if (i.getAmt() <= 0) {
                    ti.setTint(Color.RED);
                }

                inventory.add(new InventoryStack(ti, 1));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return inventory;
    }

    public boolean isOpen() {
        float time = DedicatedServer.get(EnvironmentService.class).getDayPercent();

        if (this.open > 0 && this.close > 0) {
            return (time >= this.open && time <= this.close);
        }

        return true;
    }

    public JsonObject toJson(HiveNetConnection connection) {
        JsonObject o = new JsonObject();
        o.addProperty("uid", uid);
        o.addProperty("name", this.name);
        o.addProperty("isOpen", this.isOpen());

        JsonArray items = new JsonArray();
        for (ShopItem i : this.items) {
            items.add(i.toJson());
        }

        o.add("items", buildShopInventory(connection).toJson());

        return o;
    }

}
