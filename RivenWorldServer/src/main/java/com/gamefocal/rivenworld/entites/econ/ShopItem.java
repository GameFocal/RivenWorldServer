package com.gamefocal.rivenworld.entites.econ;

import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.google.gson.JsonObject;

public class ShopItem {

    private Class<? extends InventoryItem> item;
    private float sell;
    private float buy;
    private int amt;

    public ShopItem(Class<? extends InventoryItem> item, float sell, float buy, int amt) {
        this.item = item;
        this.sell = sell;
        this.buy = buy;
        this.amt = amt;
    }

    public Class<? extends InventoryItem> getItem() {
        return item;
    }

    public void setItem(Class<? extends InventoryItem> item) {
        this.item = item;
    }

    public float getSell() {
        return sell;
    }

    public void setSell(float sell) {
        this.sell = sell;
    }

    public float getBuy() {
        return buy;
    }

    public void setBuy(float buy) {
        this.buy = buy;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        try {
            o.add("item", this.item.newInstance().toJson());
            o.addProperty("sellFor", this.sell);
            o.addProperty("buyFor", this.buy);
            o.addProperty("inStock", this.amt);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

}
